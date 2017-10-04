package com.proper.enterprise.platform.webapp

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebAppController {

    @AuthcIgnore
    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    String helloWorld() {
        """
                                                    ||                                                     
                                            |||QQQQQQQQQQQQ|||                                             
                                      |QQQQQQQQQQQQQQQQQQQQQQQQQQQQ                                        
                                    |QQ;QQQQQQQQQQQQQQQQQQQQQQQQQQpQQQ                                     
                                  |QQQQQqQQQQQQQQQQQQQQQQQQQQQQQQQ|QQQQQ                                   
                                |QQQQQQQ'QQQQQQQQQQQQQQQQQQQQQQQQpQQQQQQQQ                                 
                               QQQQQQQQQQqQQQQQQQQQQp[QQQQQQQQQQQ|QQQQQQQQQ                                
                            |QQ'QQQQQQQQQ QQQQQQQQQp[[[QQQQQQQQQrQQQQQQQQQpQQ|                             
                          |QQQQQ:QQQQQQQQQqQQQQQQQp[[Q[[QQQQQQQp|QQQQQQQQp|QQQQ|                           
                        |QQQQQQQQvQQQQQQQQ[QQQQQQp[[QQ[[[QQQQQQ"QQQQQQQQp|QQQQQQQ|                         
                      |-QQQQQQQQQQqQQQQQQQQ:QQQQp[|QQQ[Q[cQQQQpQQQQQQQQQ|QQQQQQQQQp|                       
                    |QQQ[MQQQQQQmQQqQQQQQQQQQQQp[|QQQQ|QQ[cQQQ`QQQQQQQQ|QpQQQQQQQrQQQ|                     
                   QQQQQQQmQQQQQ[[m[qQQQQQQQ'Qp[[QQpc[qQQ[[cQpQQQQQQQQ_pv[QQQQQp|QQQQQQ|                   
                 QQQQQQQQQQ_QQQQ[[[[[QQQQQQQQm[[[MQQ[[[QQ[[[c|QQQQQQQr[[[[QQQQpQQQQQQQQQQ|                 
               |QQQQQQQQQQQQ|mQp[[[[[[mmmcc[[[[[[qQQ[[QQQ[[[[[[ccmmpv[[[[[[QM|QQQQQQQQQQQQQ                
              |QQQQQQQQQQQQQQQ_p[[[[[[[[[[[[[[[[[[QMMMMMr[[[[[[[[[[[[[[[[[[p|QQQQQQQQQQQQQQQ               
              QQQQQQQQcmMQQQQQQ[[[[[[[[[[[[[[[[[[[[mmmm[[[[[[[[[[[[[[[[[[[[QQQQQQMmc[QQQQQQQ               
             |QQQQQQQQ[[[[cmc[[[[[[[[[[[[[[[[[|[||QQQQQQQ|[|[[[[[[[[[[[[[[[[[vmv[[[[[QQQQQQQQ              
             QQQQQQQQQ[[[[[[[[[[[[[[|QQQQQQQQQQQQQQMMMMQQQ`QQQQQQ|QQQQ[[[[[[[[[[[[[[[QQQQQQQQ              
            |Q|_mmMQQQ[[[[[[[[[|QQQQmMMpm-mc___`           ___vm_mmMMp|QQQ|[[[[[[[[[[QQQpmv|QQ             
            QQQQQQQ|_v[[[[[[|QQQQpc`    ||[|  |QQQpQQQQQQQq |QQQ|      _mQQQQQ[[[[[[[v|QQQQQQQ             
           |QQQQQQQQQQ[[|[:mMQpr`||[| QQQQQQQ QQQQQQQQQQQQ[|QQQQQQ|QQQQ|  _mQMpo||[[[QQQQQQQQQQ            
           QQQQQQQQQQQQQQQQp   |QQQQQQQQQQQQQ[QQQQQQQmqQQmQpQQQQQq{QQQQQQ QQQ|+QQQQQQQQQQQQQQQQ            
           mmMMMMMMMMMMMp_QQQm QQQQQQq{QQQQQQ`QQQqQQp_QQQp_QQQ[QQQQQQMQQQ|QQQQQQQmMMMMMMMMMMMpmr           
          QQQQQQQQQQQQ  |QQQQQQQQQpQQQ;qQQQQQ`QQQqQQp[QQQL QQQ[QQQpQQ[QQQ{QQQQQQQQL QQQQQQQQQQQQ           
         |QQQQQQQQQQQQQ {QQMQQQ;qQQqQQ[QQQqQQLQQQQQQp[QQQL QQQQQQpQQQ[QQQQQQQQQQQQ` QQQQQQQQQQQQQ          
         QQQQQQQQQQQQQQ ;QQQqQQCQQQQQQQQQQqQQQQQQQQQ [QQQL [QQQQQLQQQQQQpQQQQ[QQQQ |QQQQQQQQQQQQQ          
        |QQQQQQQQQQpmc|  QQQcQQQQQQ[QQQQQQ[QQQ.QQQQQL|QQpL QQQQQQ_QQQQQQ_QQQQQQQQp |vmmQQQQQQQQQQQ         
        QQQQMMmr|QQQQQQQ QQQQQQQQQQQQQqQQQQQQQQm|mQQp|QQQ` QQQQQQ|QQQQQQ|QQQQQQQQ" QQQMQQ|_mmMQQQQ         
       !m_|QQQQQQQ_`vQQQ ;qQQQQQ{qQQQQQQQQQQprQQQ[QQp|QQQ`|QQQQQQQQQMQQQQQQQQQQQQ |Qp  _QQQQQQQ|_vn        
       {QQQQQQQQM`   QQQ  QQQQQQQQQQqQQ{QQQp_ QQQ[QQp|QQQ`{QQpQQQQQQ[QQQQQQQQQQQp QQp   'QQQQQQQQQQ        
       'QQQQQQQp`    {QQL QQQ[QQQQQQQQQQQQQL  QQQ[QQp|QQQ`{QQ[QQQQQQQQpLQQpQQQQp" QQ`     qQQQQQQQp        
        QQQQQQp      'mm_ ?qQQQQQQQQ[QQQQQQL  {QQQQQp|QQQ`{QQQQQQQQQQQQ|QQQQQQQQ |vm       QQQQQQQ"        
        qQMMmmn       QQQ  QQQqQQ\$qQQQQQQQQQ   QmmmQ_{mmQ 'QmMMQ_|QQQQQQQQ|QQQQQ QQp      |vmmMMMQ         
         QQQQQQQ      QQQ[ QQQQQQCQQQpQrQQWr    `_`    `    vvc_ QQQmMQpQQQQQQp" QQp     |QQQQQQQ[         
         QQQQQQQQ     {QQQ qqQQMQ`_wwc`                            `_v_UQmqpQQQ  QQ     |QQQQQQQQ`         
         {QQQQQQQQ    'QQQ  MQQw_                                        _vMQmQ jQQ    |QQQQQQQQQ          
         'QQQQQQQQQ    mmm                                                   `  cmp   [QQQQQQQQQp          
          m__|||QQQQQQ|QQQ                                                      qQQQQQQQQQ|||__m`          
          {QQQQQQQQQQQQQQp                                                       QQQQQQQQQQQQQQQ           
           mmmmmmmmmmMMMM               [                       [                qMMMpmmmmmmmmmr           
           QQQQQQQQQQQQQQ               ^[                     [`                 QQQQQQQQQQQQQ"           
           mmMQQQQQQQQQQQ      qpmBM[[[| ;                     - ||[[QmwM&       |QQQQQQQQQQQpp`           
           QQQQ|[vmqMQQQQ         _vqQm|B[                     [M|mQMm__         {QQQQMmm_|QQQQ            
           qQQQQQQQQQQ|_v              `vmM      ^      `     Mnv`               c_|QQQQQQQQQQQ            
           qQQQQQQQQQQQQQQ                                                       QQQQQQQQQQQQQQ            
           _mmQQQQQQQQQQQQ                      [        [                      QQQQQQQQQQQQMm_            
           ;QQQ|mMQQQQQQQQQ                     `        '                     |QQQQQQQQMm_|QQQ            
            QQQQQQQ[mmQQQQQQ                   [          [                   |QQQQQMm_|QQQQQQp            
            vmQQQQQQQQQ|mmQQ                   [          [[                  QQMm_QQQQQQQQQMm_            
            QQ|_mMQQQQQQQQQ|v                 [`          '[                 |_QQQQQQQQQQpr|QQp            
            QQQQQQ|vmQQQQQQQQQ               [[            [[               |QQQQQQQQpm|QQQQQQL            
            _mMQQQQQQQ_mMQQQQQQ              [[            -[              |QQQQQMpv|QQQQQQQp_             
            QQ|_mQQQQQQQQ|_mQQQQ            [[-             [[            |QQQpc|QQQQQQQQpr|QQ`            
            qQQQQ|vqQQQQQQQQQ|mMQ           [;              -[[          |Mm_|QQQQQQQQMm`|QQQQ             
            QqQQQQQQ'mQQQQQQQQQQ|                                        |QQQQQQQQQQm_|QQQQQQ|L            
            QQmQQQQQQQ|_mQQQQQQQQQ          mmmmmmmmmmmmmmmmmB_          QQQQQQQQpv|QQQQQQQp|Qp            
            qQQvQQQQQQQQQ|vqQQQQQQ            mQBBmBBBBBBB|M_           |QQQQQMm|QQQQQQQQQp|QQ             
            'QQQ_QQQQQQQQQQQ|mMQQQ              v&BBBBBBQm`             QQQQm_|QQQQQQQQQQpQQQp             
             qQQQ_QQQQQQQQQQQQQ_mQ[     |cccccQ   _q||&c  |cccccm       Qpv|QQQQQQQQQQQQrQQQQ              
              QQQQ[qQQQQQQQQQQQQQ|"     |[[[[[[Q     _    |[[[[[[Q      |QQQQQQQQQQQQQQrQQQQp              
              qQQQQ[qQQQQQQQQQQQQQQ     |[[[Q[[Q          |[[[Q[[Q  [  'QQQQQQQQQQQQQQ_QQQQQ               
               QQQQQ|mQQQQQQQQQQQQp  [[ |[[[Q[[Q  |pvvQ   |[[[Q[[Q  [   QQQQQQQQQQQQp`QQQQQr               
               qQQQQQQvQQQQQQQQQQQ`  [[ |[[[r[[p  Q[p[[   |[[[Q[[Q [[   QQQQQQQQQQQp|QQQQQQ                
                QQQQQQQ_QQQQQQQQQQ`  [[||[[[[[Q   q[c|WQQ |[[[Q[[Q|;[[  QQQQQQQQQQp|QQQQQQr                
                qQQQQQQQ_QQQQQQQQQ  [[`Q|[[[Q[[Q |r|[[Q[| |[[[Q[[Qq [[  qQQQQQQQQp|QQQQQQQ                 
                 QQQQQQQQ'QQQQQQQQ  [;|Q|[[[Q[[Q {[Q_Q[[p |[[[Q[[Q{Q'[  qQQQQQQQr|QQQQQQQ"                 
                 cQQQQQQQQ qQQQQQp  [ QQ|[[[Q[[Q _Q[v[|[q||[[[p[[Q{Q ;[ 'QQQQQQ_QQQQQQQQp                  
                  QQQQQQQQQ qQQQQp [`|QQ|[[[Q[[Q   vmc`vvv|[[[[[[p{QQ [  QQQQp`QQQQQQQQQ"                  
                  :QQQQQQQQQ|mQQQ" ; QQQ!QQQQQQp          :QQQQQW`{QQ `  QQQp QQQQQQQQQp                   
                   QQQQQQQQQQ|cQQ`  |QQQQQ QQQQQ|QQQQQQQQQ|QQQQ QQQQQQ   QQp QQQQQQQQQQ`                   
                   :QQQQQQQQQQ|_Q   QQQQQQ`QQQQQ{QQQQQQQQQ{QQQQ`QQQQQQ   qp|QQQQQQQQQQp                    
                    QQQQQQQQQQQQ'  |QQQQQQ QQQQQ{QQQQQQQQQ{QQQQ`QQQQQQQ  .|QQQQQQQQQQQ`                    
                    :QQQQQQQQQQQ[  QQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQ[  QQQQQQQQQQQp                     
                     QQQQQQQQQQQ` |QQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQ  QQQQQQQQQQQ`                     
                     ^QQQQQQQQQQ  QQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQ[ qQQQQQQQQQp                      
                      qQQQQQQQQQ |QQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQ ;QQQQQQQQQ                       
                      'QQQQQQQQQ QQQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQ[ QQQQQQQQp                       
                       qQQQQQQQp|QQQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQQ QQQQQQQQ                        
                       'QQQQQQQ`QQQQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQQQqQQQQQQp                        
                        qQQQQQQ|QQQQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQQQ{QQQQQQ                         
                         QQQQQQQQQQQQQQQQQ QQQQQqQQQQQQQQQ{QQQQ`QQQQQQQQQQQQQQQQQr                         
                                                                                                           
                                                                                                           
                                                                                                           
                                                                                                           
                                                                                                           
         QQQQ|                                     QQ      |QQQQ                            |              
         Q   Q                                     Q |L    Q`  _Q                           Q              
         Q   Q`{Qp QmmQ |QmmQ |QmQ[ QQp|pmQ  QmmQ mQrmp`   Q`  |Q QQp QmqQ |pmQ |QmQ[ QQmQ oQn             
         QWWpr {" |p  {L|p  Q Q|||Q Q` ;Q| `|p  {L Q |L    QQWWp` Q` |[||Q qQ|' Q|||Q Q  ;L Q              
         Q     {` ;L  |L|L  Q`Q____ Q   _vqQ;L  |L Q |L    Q`     Q  {c___  _mQ Q____ Q  [p Q              
         Q     {`  Q||Q |Q||Q vQ||p Q  q[ |p Q||Q  Q !Q    Q`     Q  .Q||Q Q| |rvQ||p Q  [p Q|             
         _     _`   vc` |Lvc   _c_  _   _c_   vc`  _  _`   _`     _   'v_   vc_  _c_  _  '_ __             
                        {L                                                                                 
                                                                                                           
                                                                                                           
                                                                                                           
   QQQQ|                                 |    |C             |QQQ|                   |                     
   Q   qL                                {`   |L            |Q_  vQ                  Q                     
   Q   |p QmmQ Q  Q  Q QpmQ  Qp`|pmQ  QpmQ`   |QmmQ Q  |p   Q"    Q |Qmq[ QQmQ  QmqQ:Qm QmmQ  Qp`QQmQQmQ   
   QWWpp {"  Q q |;[|r p|||L p  Q|||Q|p  q`   |p  Q ?L Q    Q     Q`Q   Q Q   Q Q| _ Q |p  {L p  Q  Q` |"  
   Q     Q   Q`'QQ QQ 'p____ p  Q____{L  {`   |L  Q` Q|p    q[    Q Q   Q Q   Q 'vmQ Q ;L  |L p  Q  Q  |"  
   Q     :Q||Q  Qp Qp  Q||Q` p  q||Qr Q||Q`   |Q||Q  qQ`     Q|||Qr qQ||p Q[|Qr'Q |Q Q| Q||Q  p  Q  Q  |"  
   _      _v_   _` __   _c`  _   _c_   v_.`   .`vc    p       _cc`   _c_  Q_c_  _vc   _  vc`  _  _  _  .`  
                                                    |Q`                   Q                                
"""
    }

}
