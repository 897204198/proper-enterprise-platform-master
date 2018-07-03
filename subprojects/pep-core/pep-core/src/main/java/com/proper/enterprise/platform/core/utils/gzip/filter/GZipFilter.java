package com.proper.enterprise.platform.core.utils.gzip.filter;

import com.proper.enterprise.platform.core.utils.gzip.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.Deflater;

public class GZipFilter extends UserAgentFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipFilter.class);

    protected ServletContext _context;
    protected final Set<String> _mimeTypes=new HashSet<>();
    protected boolean _excludeMimeTypes;
    protected int _bufferSize=8192;
    protected int _minGzipSize=256;
    protected int _deflateCompressionLevel= Deflater.DEFAULT_COMPRESSION;
    protected boolean _deflateNoWrap = true;

    protected final Set<String> _methods=new HashSet<String>();
    protected Set<String> _excludedAgents;
    protected Set<Pattern> _excludedAgentPatterns;
    protected Set<String> _excludedPaths;
    protected Set<Pattern> _excludedPathPatterns;
    protected String _vary="Accept-Encoding, User-Agent";

    public final static String GZIP="gzip";
    public final static String ETAG_GZIP="--gzip\"";
    public final static String DEFLATE="deflate";
    public final static String ETAG_DEFLATE="--deflate\"";
    public final static String ETAG="o.e.j.s.GzipFilter.ETag";

    private static final int STATE_SEPARATOR = 0;
    private static final int STATE_Q = 1;
    private static final int STATE_QVALUE = 2;
    private static final int STATE_DEFAULT = 3;

    // non-static, as other GzipFilter instances may have different configurations
    protected final ThreadLocal<Deflater> _deflater = new ThreadLocal<Deflater>();
    protected final static ThreadLocal<byte[]> _buffer= new ThreadLocal<byte[]>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        super.init(filterConfig);

        _context=filterConfig.getServletContext();

        String tmp=filterConfig.getInitParameter("bufferSize");
        if (tmp!=null) {
            _bufferSize = Integer.parseInt(tmp);
        }

        tmp=filterConfig.getInitParameter("minGzipSize");
        if (tmp!=null) {
            _minGzipSize = Integer.parseInt(tmp);
        }

        tmp=filterConfig.getInitParameter("deflateCompressionLevel");
        if (tmp!=null) {
            _deflateCompressionLevel = Integer.parseInt(tmp);
        }
        tmp=filterConfig.getInitParameter("deflateNoWrap");
        if (tmp!=null) {
            _deflateNoWrap = Boolean.parseBoolean(tmp);
        }
        tmp=filterConfig.getInitParameter("methods");
        if (tmp!=null){
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens())
                _methods.add(tok.nextToken().trim().toUpperCase(Locale.ENGLISH));
        } else {
            _methods.add("GET");
        }
        tmp=filterConfig.getInitParameter("mimeTypes");
        if (tmp==null){
            _excludeMimeTypes=true;
            tmp=filterConfig.getInitParameter("excludedMimeTypes");
            if (tmp==null){
                _mimeTypes.add("application/compress");
                _mimeTypes.add("application/zip");
                _mimeTypes.add("application/gzip");
            }
            else{
                StringTokenizer tok = new StringTokenizer(tmp,",",false);
                while (tok.hasMoreTokens()) {
                    _mimeTypes.add(tok.nextToken().trim());
                }
            }
        }
        else{
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens()) {
                _mimeTypes.add(tok.nextToken().trim());
            }
        }
        tmp=filterConfig.getInitParameter("excludedAgents");
        if (tmp!=null){
            _excludedAgents=new HashSet<String>();
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens()) {
                _excludedAgents.add(tok.nextToken().trim());
            }
        }

        tmp=filterConfig.getInitParameter("excludeAgentPatterns");
        if (tmp!=null){
            _excludedAgentPatterns=new HashSet<Pattern>();
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens()) {
                _excludedAgentPatterns.add(Pattern.compile(tok.nextToken().trim()));
            }
        }

        tmp=filterConfig.getInitParameter("excludePaths");
        if (tmp!=null){
            _excludedPaths=new HashSet<String>();
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens()) {
                _excludedPaths.add(tok.nextToken().trim());
            }
        }

        tmp=filterConfig.getInitParameter("excludePathPatterns");
        if (tmp!=null){
            _excludedPathPatterns=new HashSet<Pattern>();
            StringTokenizer tok = new StringTokenizer(tmp,",",false);
            while (tok.hasMoreTokens()) {
                _excludedPathPatterns.add(Pattern.compile(tok.nextToken().trim()));
            }
        }

        tmp=filterConfig.getInitParameter("vary");
        if (tmp!=null) {
            _vary = tmp;
        }
    }

    @Override
    public void destroy() { }

    /**
     * Checks to see if the path is excluded
     *
     * @param requestURI
     *            the request uri
     * @return boolean true if excluded
     */
    private boolean isExcludedPath(String requestURI)
    {
        if (requestURI == null)
            return false;
        if (_excludedPaths != null)
        {
            for (String excludedPath : _excludedPaths)
            {
                if (requestURI.startsWith(excludedPath))
                {
                    return true;
                }
            }
        }
        if (_excludedPathPatterns != null)
        {
            for (Pattern pattern : _excludedPathPatterns)
            {
                if (pattern.matcher(requestURI).matches())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks to see if the userAgent is excluded
     *
     * @param ua
     *            the user agent
     * @return boolean true if excluded
     */
    private boolean isExcludedAgent(String ua)
    {
        if (ua == null)
            return false;

        if (_excludedAgents != null)
        {
            if (_excludedAgents.contains(ua))
            {
                return true;
            }
        }
        if (_excludedAgentPatterns != null)
        {
            for (Pattern pattern : _excludedAgentPatterns)
            {
                if (pattern.matcher(ua).matches())
                {
                    return true;
                }
            }
        }

        return false;
    }

    private String[] getEncodings (String encodingHeader)
    {
        if (encodingHeader == null)
            return null;
        return encodingHeader.split(",");
    }

    private boolean isEncodingAcceptable(String encoding)
    {
        int state = STATE_DEFAULT;
        int qvalueIdx = -1;
        for (int i=0;i<encoding.length();i++)
        {
            char c = encoding.charAt(i);
            switch (state)
            {
                case STATE_DEFAULT:
                {
                    if (';' == c)
                        state = STATE_SEPARATOR;
                    break;
                }
                case STATE_SEPARATOR:
                {
                    if ('q' == c || 'Q' == c)
                        state = STATE_Q;
                    break;
                }
                case STATE_Q:
                {
                    if ('=' == c)
                        state = STATE_QVALUE;
                    break;
                }
                case STATE_QVALUE:
                {
                    if (qvalueIdx < 0 && '0' == c || '1' == c)
                        qvalueIdx = i;
                    break;
                }
            }
        }

        if (qvalueIdx < 0)
            return true;

        if ("0".equals(encoding.substring(qvalueIdx).trim()))
            return false;
        return true;
    }

    private String selectCompression(String encodingHeader)
    {
        // prefer gzip over deflate
        String compression = null;
        if (encodingHeader!=null)
        {

            String[] encodings = getEncodings(encodingHeader);
            if (encodings != null)
            {
                for (int i=0; i< encodings.length; i++)
                {
                    if (encodings[i].toLowerCase(Locale.ENGLISH).contains(GZIP))
                    {
                        if (isEncodingAcceptable(encodings[i]))
                        {
                            compression = GZIP;
                            break; //prefer Gzip over deflate
                        }
                    }

                    if (encodings[i].toLowerCase(Locale.ENGLISH).contains(DEFLATE))
                    {
                        if (isEncodingAcceptable(encodings[i]))
                        {
                            compression = DEFLATE; //Keep checking in case gzip is acceptable
                        }
                    }
                }
            }
        }
        return compression;
    }

    protected void configureWrappedResponse(CompressedResponseWrapper wrappedResponse)
    {
         IncludeExclude<String> mimeTypeExclusions = new IncludeExclude<>();
        if(_excludeMimeTypes)
            mimeTypeExclusions.getExcluded().addAll(_mimeTypes);
        else
            mimeTypeExclusions.getIncluded().addAll(_mimeTypes);

        wrappedResponse.setMimeTypes(mimeTypeExclusions);
        wrappedResponse.setBufferSize(_bufferSize);
        wrappedResponse.setMinCompressSize(_minGzipSize);
    }

    protected CompressedResponseWrapper createWrappedResponse(HttpServletRequest request, HttpServletResponse response, final String compressionType)
    {
        CompressedResponseWrapper wrappedResponse = null;
        wrappedResponse = new CompressedResponseWrapper(request,response)
        {
            @Override
            protected AbstractCompressedStream newCompressedStream(HttpServletRequest request, HttpServletResponse response) throws IOException
            {
                return new AbstractCompressedStream(compressionType,request,this,_vary)
                {
                    @Override
                    public void setWriteListener(WriteListener writeListener) {

                    }

                    private Deflater _allocatedDeflater;
                    private byte[] _allocatedBuffer;

                    @Override
                    protected OutputStream createStream() throws IOException
                    {
                        if (compressionType == null)
                        {
                            return null;
                        }

                        // acquire deflater instance
                        _allocatedDeflater = _deflater.get();
                        if (_allocatedDeflater==null)
                            _allocatedDeflater = new Deflater(_deflateCompressionLevel,_deflateNoWrap);
                        else
                        {
                            _deflater.set(null);
                            _allocatedDeflater.reset();
                        }

                        // acquire buffer
                        _allocatedBuffer = _buffer.get();
                        if (_allocatedBuffer==null)
                            _allocatedBuffer = new byte[_bufferSize];
                        else
                            _buffer.set(null);

                        switch (compressionType)
                        {
                            case GZIP:
                                return new GzipOutputStream(_response.getOutputStream(),_allocatedDeflater,_allocatedBuffer);
                            case DEFLATE:
                                return new DeflatedOutputStream(_response.getOutputStream(),_allocatedDeflater,_allocatedBuffer);
                        }
                        throw new IllegalStateException(compressionType + " not supported");
                    }

                    @Override
                    public void finish() throws IOException
                    {
                        super.finish();
                        if (_allocatedDeflater != null && _deflater.get() == null)
                        {
                            _deflater.set(_allocatedDeflater);
                        }
                        if (_allocatedBuffer != null && _buffer.get() == null)
                        {
                            _buffer.set(_allocatedBuffer);
                        }
                    }
                };
            }
        };
        configureWrappedResponse(wrappedResponse);
        return wrappedResponse;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;

        // If not a supported method or it is an Excluded URI - no Vary because no matter what client, this URI is always excluded
        String requestURI = request.getRequestURI();
        if (!_methods.contains(request.getMethod()) || isExcludedPath(requestURI))
        {
            super.doFilter(request,response,chain);
            return;
        }

        // Exclude non compressible mime-types known from URI extension. - no Vary because no matter what client, this URI is always excluded
        if (_mimeTypes.size()>0 && _excludeMimeTypes)
        {
            String mimeType = _context.getMimeType(request.getRequestURI());

            if (mimeType!=null)
            {
                mimeType = getContentTypeWithoutCharset(mimeType);
                if (_mimeTypes.contains(mimeType))
                {
                    // handle normally without setting vary header
                    super.doFilter(request,response,chain);
                    return;
                }
            }
        }

        //If the Content-Encoding is already set, then we won't compress
        if (response.getHeader("Content-Encoding") != null)
        {
            super.doFilter(request,response,chain);
            return;
        }

        // Excluded User-Agents
        String ua = getUserAgent(request);
        boolean ua_excluded=ua!=null&&isExcludedAgent(ua);

        // Acceptable compression type
        String compressionType = ua_excluded?null:selectCompression(request.getHeader("accept-encoding"));

        // Special handling for etags
        String etag = request.getHeader("If-None-Match");
        if (etag!=null)
        {
            int dd=etag.indexOf("--");
            if (dd>0) {
                request.setAttribute(ETAG, etag.substring(0, dd) + (etag.endsWith("\"") ? "\"" : ""));
            }
        }

        CompressedResponseWrapper wrappedResponse = createWrappedResponse(request,response,compressionType);

        boolean exceptional=true;
        try
        {
            super.doFilter(request,wrappedResponse,chain);
            exceptional=false;
        }
        finally
        {
            if (request.isAsyncStarted())
            {
                request.getAsyncContext().addListener(new FinishOnCompleteListener(wrappedResponse));
            }
            else if (exceptional && !response.isCommitted())
            {
                wrappedResponse.resetBuffer();
                wrappedResponse.noCompression();
            }
            else
                wrappedResponse.finish();
        }
    }

    public static String getContentTypeWithoutCharset(String value)
    {
        int end=value.length();
        int state=0;
        int start=0;
        boolean quote=false;
        int i=0;
        StringBuilder builder=null;
        for (;i<end;i++)
        {
            char b = value.charAt(i);

            if ('"'==b)
            {
                if (quote)
                {
                    quote=false;
                }
                else
                {
                    quote=true;
                }

                switch(state)
                {
                    case 11:
                        builder.append(b);break;
                    case 10:
                        break;
                    case 9:
                        builder=new StringBuilder();
                        builder.append(value,0,start+1);
                        state=10;
                        break;
                    default:
                        start=i;
                        state=0;
                }
                continue;
            }

            if (quote)
            {
                if (builder!=null && state!=10)
                    builder.append(b);
                continue;
            }

            switch(state)
            {
                case 0:
                    if (';'==b)
                        state=1;
                    else if (' '!=b)
                        start=i;
                    break;

                case 1: if ('c'==b) state=2; else if (' '!=b) state=0; break;
                case 2: if ('h'==b) state=3; else state=0;break;
                case 3: if ('a'==b) state=4; else state=0;break;
                case 4: if ('r'==b) state=5; else state=0;break;
                case 5: if ('s'==b) state=6; else state=0;break;
                case 6: if ('e'==b) state=7; else state=0;break;
                case 7: if ('t'==b) state=8; else state=0;break;
                case 8: if ('='==b) state=9; else if (' '!=b) state=0; break;

                case 9:
                    if (' '==b)
                        break;
                    builder=new StringBuilder();
                    builder.append(value,0,start+1);
                    state=10;
                    break;

                case 10:
                    if (';'==b)
                    {
                        builder.append(b);
                        state=11;
                    }
                    break;
                case 11:
                    if (' '!=b)
                        builder.append(b);
            }
        }
        if (builder==null)
            return value;
        return builder.toString();

    }
}
