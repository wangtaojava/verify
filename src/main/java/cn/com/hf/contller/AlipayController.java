/**
 * llin 2019年4月10日下午9:53:22
 */
package cn.com.hf.contller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

/**
 * @author llin
 */
@Controller
public class AlipayController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwn5dM6Wk/quGmsh1qMoHplP2awR4Ci/kGNfXF97C4NvrTdSvykqr/pRe7lh6VuxifDwdVio/Zxlh0EQwK6+IiFa+Hl2/dNMgE281MzM2ICC4ogdj/DM2WqxTHK4sXXrd80sKdvulByNxfc//fl63aonCCUzP3NlWCVnKWny5eq1atHb3lMHSJRLFtNiJ2MyshdrZqL9CV4mmbbkbsdpEYyF7dY6nJTHRuDNcakr6hVxLbZ+K1P+EOzM96WoB7AxREtr6KOw98PJ+csBNQDTvQ0aZyPGfVRNjb0GLEL4P/SUKcliFpcSlJHr7qq3Wq0YtGOoOvvc4CKJls3Z+ZzH+LwIDAQAB";

	 public static final String PRIVATE_KEY       = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5BvVI+m7VD5Xza27FtqaAwCzEdPJzrGhEQ78IB+D/u18pAuQTnLktrsxZB5q97Pqv8aEv8LLH9r6fT8BQIYv3Gab6VZs1QxPYAcnZWEgAWdYpnAeY8pebgSUIWW2P5xrqi067KQFAv+WD9wJEjWuTOayFHsbe+Sue89dHT7jdb9wnd60WLt8k4GK1wYzIZ/djibdDyOOufFN8XYJHH3i6TGA7lCGKbdyhfc004EL6v3TQwhb5aHGQo1TPvKSxnsR90y+NgK2yI6WDCyhT8ubmdkyaEApSiPOvdogRgp3AOGcEk0Pd9trnxP3M9IzdakGLvC10nSaJQNVf8relRsSbAgMBAAECggEAMiTJmtKL8N/9cm++IIUEkMYEG+XZwzGJPF7jyahg+M1t16554zrJELFbnhEqhahQtBgW5+gie4DKXmB9rPm6degU0akbnA6mtRae/nEnajjuim5HvNKZ9cHFKIMb5zDpoYDJgH13YnXfHKl3it6MteSedaFGoi0C/lv1DtVzM0dkJo+UAcZcTm0SKc//YiB/F6e+kvk6AC/K1GSdGRm67MLniJ8dt2puyNKZzZHI70tQGmgocF85f4AuEchqG1rG4p/8aUCwF/A3w+D18QMYXP0R3K/9kzmcuV2K6ncRMFuJedqKpTv7+roB2JnFztEdGOUSmEL0XAsEH7rRZ/HEqQKBgQDqexKSlOFSXxEoa6joijL4jv2sTUdXeduLxKgSYu984oyBGciCS9ZGYX9rEZLr/AEsi3Gt2lZ67DcJA4/uDD6t4PtkLaeaS7RxPnqp0ipR1aMlTjShIgfjfsaDBa6QA/cxXxSTYLGVPdGwOahf3B+Dxmor79FLwSrzt9yDB0IzvQKBgQDKAgQBXNh7P7MrmolH825UX3BZ2kXNvP+FQPo9k9znRHlIsI2CS1iVCRnj0IuOW0oaWmIOGzKgZS5H0eeAPOvwp+ZoyCwUJ6//YgegDD1078Sn02787QCnN1tzd/z+RyI8tBh5sSo291ZUpMlo4pPkPZXB2VpeC9pSclqJ2SszNwKBgQC5TpRJ9ZDwuSLlL9goFnfbyZ2emuAjuLESD1PIXBnqN1xU9txoZKitrIW3Rwc2qkz/OCCaBbBMqvt/iyonNdQfNGtYK2CGZwfuazMjnG1HiS/K3rhnE27qkhkuGMJxHEEx6nNnWYZebKGXxKlVQLBDQv5LRNbCKKHaRFbn0ke2fQKBgGgq/+u/9/YeVANBLleHBTbG/EWpU+OS+WS6rBAC5p/vZGeCe4Zb2jMIPWyHxt9C3gSW5QeHKOUjJ+lrKWisgbJYsm0IBArkqFIWN/5lMW/SkGNGmW4dvP99gVAa6MAq6FHLQO/H1w//HWqKiz3l0QOaalZLAdmLXSq0w1FgRnGjAoGBAM/yCwrMZGTx2HUAQQW85axCWBBsBpjde6xGYrr/XPPtPJxNim+JMe7z+AGIr5Y25qJINsETGoQDBVZpX/A/eBxlJqz4Cn6WC2dzDoXtgZWZ6vLuqsEI5fvZrGIqpgAm4c3pYdhczcLemTzqzvMA574dPLOO6mfxCMkNL763Yqaf";
	    /**字符编码-传递给支付宝的数据编码*/
	    public static final String CHARSET           = "GBK";

	    /**签名类型-视支付宝服务窗要求*/
	    public static final String SIGN_TYPE         = "RSA2";
	
	@RequestMapping("/alipay")
	@ResponseBody
	public String register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("this is alipay...");
		
		   //支付宝响应消息  
        String responseMsg = "00";

        try {
			//1. 解析请求参数
			Map<String, String> params = getRequestParams(request);
			
			//打印本次请求日志，开发者自行决定是否需要
			logger.info("支付宝请求串", params.toString());
			
			
     //对响应内容加签
			responseMsg = AlipaySignature.encryptAndSign(responseMsg,ALIPAY_PUBLIC_KEY, PRIVATE_KEY, CHARSET, false, true, SIGN_TYPE);
		} catch (Exception e) {
			logger.error(null,e);
		}
        
      //http 内容应答
        response.reset();
        response.setContentType("text/xml;charset=GBK");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseMsg);
        response.flushBuffer();
		
		return responseMsg;
	}
	
    /**
     * 获取所有request请求参数key-value
     * 
     * @param request
     * @return
     */
    private static Map<String, String> getRequestParams(HttpServletRequest request){
        
        Map<String, String> params = new HashMap<String, String>();
        if(null != request){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String key : paramsKey){
                params.put(key, request.getParameter(key));
            }
        }
        return params;
    }
	
}
