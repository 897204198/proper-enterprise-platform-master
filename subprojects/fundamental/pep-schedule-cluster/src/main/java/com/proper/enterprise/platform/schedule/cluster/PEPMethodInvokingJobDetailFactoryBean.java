package com.proper.enterprise.platform.schedule.cluster;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.*;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;

import java.io.Serializable;

/**
 * <p><b>NOTE: JobDetails created via MethodInvokingFactoryBean are <i>not</i>
 * serializable and thus not suitable for persistent job stores.</b>
 * So implement this one. The list below is the difference to origin version:
 * 1. Not implement BeanClassLoaderAware. Clean beanClassLoader, setBeanClassLoader and resolveClassName
 * 2. Implement Serializable
 * 3. Add targetMethod property and getter/setter
 * 4. Use application context to get bean in executeInternal method of MethodInvokingJob
 */
public class PEPMethodInvokingJobDetailFactoryBean extends ArgumentConvertingMethodInvoker
    implements FactoryBean<JobDetail>, BeanNameAware, BeanFactoryAware, InitializingBean, Serializable {

    //CHECKSTYLE:OFF

    private String name;

    private String group = Scheduler.DEFAULT_GROUP;

    private boolean concurrent = true;

    private String targetBeanName;

    private String beanName;

    private BeanFactory beanFactory;

    private JobDetail jobDetail;

    private String targetMethod;

    private Object[] arguments = new Object[0];

    /**
     * Set the name of the job.
     * <p>Default is the bean name of this FactoryBean.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the group of the job.
     * <p>Default is the default group of the Scheduler.
     * @see Scheduler#DEFAULT_GROUP
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Specify whether or not multiple jobs should be run in a concurrent fashion.
     * The behavior when one does not want concurrent jobs to be executed is
     * realized through adding the {@code @PersistJobDataAfterExecution} and
     * {@code @DisallowConcurrentExecution} markers.
     * More information on stateful versus stateless jobs can be found
     * <a href="http://www.quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-03">here</a>.
     * <p>The default setting is to run jobs concurrently.
     */
    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    /**
     * Set the name of the target bean in the Spring BeanFactory.
     * <p>This is an alternative to specifying {@link #setTargetObject "targetObject"},
     * allowing for non-singleton beans to be invoked. Note that specified
     * "targetObject" and {@link #setTargetClass "targetClass"} values will
     * override the corresponding effect of this "targetBeanName" setting
     * (i.e. statically pre-define the bean type or even the bean object).
     */
    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    /**
     * Set the parameters of the target mehod, using the object array format.
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
        prepare();

        // Use specific name if given, else fall back to bean name.
        String name = (this.name != null ? this.name : this.beanName);

        // Consider the concurrent flag to choose between stateful and stateless job.
        Class<?> jobClass = (this.concurrent ? MethodInvokingJob.class : StatefulMethodInvokingJob.class);

        // Build JobDetail instance.
        JobDetailImpl jdi = new JobDetailImpl();
        jdi.setName(name);
        jdi.setGroup(this.group);
        jdi.setJobClass((Class) jobClass);
        jdi.setDurability(true);
        jdi.getJobDataMap().put("methodInvoker", this);
        this.jobDetail = jdi;

        postProcessJobDetail(this.jobDetail);
    }

    /**
     * Callback for post-processing the JobDetail to be exposed by this FactoryBean.
     * <p>The default implementation is empty. Can be overridden in subclasses.
     * @param jobDetail the JobDetail prepared by this FactoryBean
     */
    protected void postProcessJobDetail(JobDetail jobDetail) {
    }


    /**
     * Overridden to support the {@link #setTargetBeanName "targetBeanName"} feature.
     */
    @Override
    public Class<?> getTargetClass() {
        Class<?> targetClass = super.getTargetClass();
        if (targetClass == null && this.targetBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
            targetClass = this.beanFactory.getType(this.targetBeanName);
        }
        return targetClass;
    }

    /**
     * Overridden to support the {@link #setTargetBeanName "targetBeanName"} feature.
     */
    @Override
    public Object getTargetObject() {
        Object targetObject = super.getTargetObject();
        if (targetObject == null && this.targetBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
            targetObject = this.beanFactory.getBean(this.targetBeanName);
        }
        return targetObject;
    }


    @Override
    public JobDetail getObject() {
        return this.jobDetail;
    }

    @Override
    public Class<? extends JobDetail> getObjectType() {
        return (this.jobDetail != null ? this.jobDetail.getClass() : JobDetail.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public String getTargetMethod() {
        return targetMethod;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    /**
     * Quartz Job implementation that invokes a specified method.
     * Automatically applied by MethodInvokingJobDetailFactoryBean.
     */
    public static class MethodInvokingJob extends QuartzJobBean {

        private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvokingJob.class);

        /**
         * @see SchedulerFactoryBean#setApplicationContextSchedulerContextKey
         */
        private static final String APPLICATION_CONTEXT_SCHEDULER_CONTEXT_KEY = "applicationContext";

        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            String mTargetBean = "";
            String mTargetMethod = "";
            try {
                ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_SCHEDULER_CONTEXT_KEY);

                PEPMethodInvokingJobDetailFactoryBean factoryBean = (PEPMethodInvokingJobDetailFactoryBean) context.getMergedJobDataMap().get("methodInvoker");
                mTargetBean = factoryBean.targetBeanName;
                mTargetMethod = factoryBean.targetMethod;

                Object targetBean = applicationContext.getBean(mTargetBean);

                Object[] arguments = factoryBean.arguments;
                MethodInvoker methodInvoker = new MethodInvoker();
                methodInvoker.setTargetObject(targetBean);
                methodInvoker.setTargetMethod(mTargetMethod);
                methodInvoker.setArguments(arguments);
                methodInvoker.prepare();

                methodInvoker.invoke();
            } catch (Exception e) {
//                String errorMessage = "Could not invoke method '" + mTargetMethod + "' on target bean [" + mTargetBean + "]";
//                LOGGER.error(errorMessage, e);
//                throw new JobExecutionException(errorMessage, e, false);
            }
        }
    }


    /**
     * Extension of the MethodInvokingJob, implementing the StatefulJob interface.
     * Quartz checks whether or not jobs are stateful and if so,
     * won't let jobs interfere with each other.
     */
    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class StatefulMethodInvokingJob extends MethodInvokingJob {

        // No implementation, just an addition of the tag interface StatefulJob
        // in order to allow stateful method invoking jobs.
    }

    //CHECKSTYLE:ON

}
