package com.spinfosec.utils;

import com.spinfosec.system.MemInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [MIME和后缀的转换]
 * @ClassName: com.spinfosec.utils.MIMETypeUtil
 * @description [MIME和后缀的转换]
 * @create 2019/1/7 19:37
 * @copyright Copyright(C) 2019 SHIPING INFO Corporation. All rights reserved.
 */
public class MIMETypeUtil
{

    private Logger logger = LoggerFactory.getLogger(MIMETypeUtil.class);

    private Map<String, String> mimeTypeMap = new HashMap<String, String>();

    private MIMETypeUtil()
    {
        String mimeTypeXmlPath = MemInfo.getServletContextPath() + "mimetypes.xml";
        try
        {
            logger.info("mimiTypeXmlPath = " + mimeTypeXmlPath);
            File file = new File(mimeTypeXmlPath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList globs = document.getElementsByTagName("glob");
            if (null != globs && globs.getLength() > 0)
            {
                for (int i = 0; i < globs.getLength(); i++)
                {
                    Node globNode = globs.item(i);
                    if (globNode.hasAttributes())
                    {
                        String pattern = globNode.getAttributes().item(0).getNodeValue();
                        if (StringUtils.isNotEmpty(pattern))
                        {
                            Node parentNode = globNode.getParentNode();
                            if (parentNode.hasAttributes())
                            {
                                String mimeType = parentNode.getAttributes().item(0).getNodeValue();
                                if (!mimeTypeMap.containsKey(mimeType))
                                {
                                    mimeTypeMap.put(mimeType, pattern);
                                }
                            }
                        }
                    }
                }
            }
            logger.info("解析mimeTypeXml后获取的map的大小为：" + mimeTypeMap.size());
        }
        catch (Exception e)
        {
            logger.error("读取xml失败" + mimeTypeXmlPath, e);
        }
    }

    private static MIMETypeUtil instance = new MIMETypeUtil();


    public static MIMETypeUtil getInstance()
    {
        if (null == instance)
        {
            instance = new MIMETypeUtil();
        }
        return instance;
    }

    /*

    static
    {
        mimeTypeMap.put("application/envoy", "*.evy");
        mimeTypeMap.put("application/fractals", "*.fif");
        mimeTypeMap.put("application/futuresplash", "*.spl");
        mimeTypeMap.put("application/hta", "*.hta");
        mimeTypeMap.put("application/internet-property-stream", "*.acx");
        mimeTypeMap.put("application/mac-binhex40", "*.hqx");
        mimeTypeMap.put("application/msword", "*.doc"); // doc/dot
        mimeTypeMap.put("application/octet-stream", "*.exe"); // exe/class/dms/bin/lha/lzh/so/iso/dmg/dist/distz/pkg/bpk/dump/elc/deploy/pcap
        mimeTypeMap.put("application/oda", "*.oda");
        mimeTypeMap.put("application/olescript", "*.axs");
        mimeTypeMap.put("application/pdf", "*.pdf");
        mimeTypeMap.put("application/pics-rules", "*.prf");
        mimeTypeMap.put("application/pkcs10", "*.p10");
        mimeTypeMap.put("application/pkix-crl", "*.crl");
        mimeTypeMap.put("application/postscript", "*.ps"); // ai/eps/ps
        mimeTypeMap.put("application/rtf", "*.rtf");
        mimeTypeMap.put("application/set-payment-initiation", "*.setpay");
        mimeTypeMap.put("application/set-registration-initiation", "*.setreg");
        mimeTypeMap.put("application/vnd.ms-excel", "*.xls"); // xls/xla/xlc/xlm/xlt/xlw
        mimeTypeMap.put("application/vnd.ms-outlook", "*.msg");
        mimeTypeMap.put("application/vnd.ms-pkicertstore", "*.sst");
        mimeTypeMap.put("application/vnd.ms-pkiseccat", "*.cat");
        mimeTypeMap.put("application/vnd.ms-pkistl", "*.stl");
        mimeTypeMap.put("application/vnd.ms-powerpoint", "*.ppt"); // ppt/pot/pps
        mimeTypeMap.put("application/vnd.ms-project", "*.mpp");
        mimeTypeMap.put("application/vnd.ms-works", "*.wcm"); // wcm/wdb/wks/wps
        mimeTypeMap.put("application/winhlp", "*.hlp");
        mimeTypeMap.put("application/x-bcpio", "*.bcpio");
        mimeTypeMap.put("application/x-cdf", "*.cdf");
        mimeTypeMap.put("application/x-compress", "*.z");
        mimeTypeMap.put("application/x-compressed", "*.tgz");
        mimeTypeMap.put("application/x-cpio", "*.cpio");
        mimeTypeMap.put("application/x-csh", "*.csh");
        mimeTypeMap.put("application/x-director", "*.dcr"); // dcr/dir/dxr
        mimeTypeMap.put("application/x-dvi", "*.dvi");
        mimeTypeMap.put("application/x-gtar", "*.gtar");
        mimeTypeMap.put("application/x-gzip", "*.gz");
        mimeTypeMap.put("application/x-hdf", "*.hdf");
        mimeTypeMap.put("application/x-internet-signup", "*.ins"); // ins/isp
        mimeTypeMap.put("application/x-iphone", "*.iii");
        mimeTypeMap.put("application/x-javascript", "*.js");
        mimeTypeMap.put("application/x-latex", "*.latex");
        mimeTypeMap.put("application/x-msaccess", "*.mdb");
        mimeTypeMap.put("application/x-mscardfile", "*.crd");
        mimeTypeMap.put("application/x-msclip", "*.clp");
        mimeTypeMap.put("application/x-msdownload", "*.dll");
        mimeTypeMap.put("application/x-msmediaview", "*.m13"); // m13/m14/mvb
        mimeTypeMap.put("application/x-msmetafile", "*.wmf");
        mimeTypeMap.put("application/x-msmoney", "*.mny");
        mimeTypeMap.put("application/x-mspublisher", "*.pub");
        mimeTypeMap.put("application/x-msschedule", "*.scd");
        mimeTypeMap.put("application/x-msterminal", "*.trm");
        mimeTypeMap.put("application/x-mswrite", "*.wri");
        mimeTypeMap.put("application/x-netcdf", "*.cdf");
        mimeTypeMap.put("application/x-netcdf", "*.nc");
        mimeTypeMap.put("application/x-perfmon", "*.pma"); // pma/pmc/pml/pmr/pmw
        mimeTypeMap.put("application/x-pkcs12", "*.p12"); // p12/pfx
        mimeTypeMap.put("application/x-pkcs7-certificates", "*.p7b"); // p7b/spc
        mimeTypeMap.put("application/x-pkcs7-certreqresp", "*.p7r");
        mimeTypeMap.put("application/x-pkcs7-mime", "*.p7c"); // p7c/p7m
        mimeTypeMap.put("application/x-pkcs7-signature", "*.p7s");
        mimeTypeMap.put("application/x-sh", "*.sh");
        mimeTypeMap.put("application/x-shar", "*.shar");
        mimeTypeMap.put("application/x-shockwave-flash", "*.swf");
        mimeTypeMap.put("application/x-stuffit", "*.sit");
        mimeTypeMap.put("application/x-sv4cpio", "*.sv4cpio");
        mimeTypeMap.put("application/x-sv4crc", "*.sv4crc");
        mimeTypeMap.put("application/x-tar", "*.tar");
        mimeTypeMap.put("application/x-tcl", "*.tcl");
        mimeTypeMap.put("application/x-tex", "*.tex");
        mimeTypeMap.put("application/x-texinfo", "*.texi"); // texi/texinfo
        mimeTypeMap.put("application/x-troff", "*.roff"); // roff/t/tr
        mimeTypeMap.put("application/x-troff-man", "*.man");
        mimeTypeMap.put("application/x-troff-me", "*.me");
        mimeTypeMap.put("application/x-troff-ms", "*.ms");
        mimeTypeMap.put("application/x-ustar", "*.ustar");
        mimeTypeMap.put("application/x-wais-source", "*.src");
        mimeTypeMap.put("application/x-x509-ca-cert", "*.cer"); // cer/crt/der
        mimeTypeMap.put("application/ynd.ms-pkipko", "*.pko");
        mimeTypeMap.put("application/zip", "*.zip");
        mimeTypeMap.put("audio/basic", "*.au"); // au/snd
        mimeTypeMap.put("audio/mid", "*.mid"); // mid/rmi
        mimeTypeMap.put("audio/mpeg", "*.mp3");
        mimeTypeMap.put("audio/x-aiff", "*.aif"); // aif/aifc/aiff
        mimeTypeMap.put("audio/x-mpegurl", "*.m3u");
        mimeTypeMap.put("audio/x-pn-realaudio", "*.ra"); // ra/ram
        mimeTypeMap.put("audio/x-wav", "*.wav");
        mimeTypeMap.put("image/bmp", "*.bmp");
        mimeTypeMap.put("image/cis-cod", "*.cod");
        mimeTypeMap.put("image/gif", "*.gif");
        mimeTypeMap.put("image/ief", "*.ief");
        mimeTypeMap.put("image/jpeg", "*.jpg"); // jpg/jpeg/jpe
        mimeTypeMap.put("image/pipeg", "*.jfif");
        mimeTypeMap.put("image/svg+xml", "*.svg");
        mimeTypeMap.put("image/tiff", "*.tif"); // tif/tiff
        mimeTypeMap.put("image/x-cmu-raster", "*.ras");
        mimeTypeMap.put("image/x-cmx", "*.cmx");
        mimeTypeMap.put("image/x-icon", "*.ico");
        mimeTypeMap.put("image/x-portable-anymap", "*.pnm");
        mimeTypeMap.put("image/x-portable-bitmap", "*.pbm");
        mimeTypeMap.put("image/x-portable-graymap", "*.pgm");
        mimeTypeMap.put("image/x-portable-pixmap", "*.ppm");
        mimeTypeMap.put("image/x-rgb", "*.rgb");
        mimeTypeMap.put("image/x-xbitmap", "*.xbm");
        mimeTypeMap.put("image/x-xpixmap", "*.xpm");
        mimeTypeMap.put("image/x-xwindowdump", "*.xwd");
        mimeTypeMap.put("message/rfc822", "*.mht"); // mht/mhtml/nws
        mimeTypeMap.put("text/css", "*.css");
        mimeTypeMap.put("text/h323", "*.323");
        mimeTypeMap.put("text/html", "*.html"); // html/htm/stm
        mimeTypeMap.put("text/iuls", "*.uls");
        mimeTypeMap.put("text/plain", "*.txt"); // txt/bas/c/h/js/mbox
        mimeTypeMap.put("text/richtext", "*.rtx");
        mimeTypeMap.put("text/scriptlet", "*.sct");
        mimeTypeMap.put("text/tab-separated-values", "*.tsv");
        mimeTypeMap.put("text/webviewhtml", "*.htt");
        mimeTypeMap.put("text/x-component", "*.htc");
        mimeTypeMap.put("text/x-setext", "*.etx");
        mimeTypeMap.put("text/x-vcard", "*.vcf");
        mimeTypeMap.put("video/mpeg", "*.mp2"); // mp2/mpa/mpe/mpeg/mpg/mpv2
        mimeTypeMap.put("video/quicktime", "*.mov"); // mov/qt
        mimeTypeMap.put("video/x-la-asf", "*.lsf"); // lsf/lsx
        mimeTypeMap.put("video/x-ms-asf", "*.asf"); // asf/asr/asx
        mimeTypeMap.put("video/x-msvideo", "*.avi");
        mimeTypeMap.put("video/x-sgi-movie", "*.movie");
        mimeTypeMap.put("x-world/x-vrml", "*.flr"); // flr/vrml/wrl/wrz/xaf/xof
        mimeTypeMap.put("application/x-tika-msoffice", "*.db");
        mimeTypeMap.put("application/java-vm", "*.class");
        mimeTypeMap.put("image/x-ms-bmp", "*.bmp"); // bmp/dib
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "*.xlsx");
        mimeTypeMap.put("application/vnd.ms-excel.sheet.macroenabled.12", "*.xlsm");
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "*.docx");
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "*.pptx"); // pptx/thmx
        mimeTypeMap.put("application/x-rar-compressed", "*.rar");
        mimeTypeMap.put("vnd.visio", "*.vsd"); // vsd/vst/vss/vsw
        mimeTypeMap.put("text/x-vbasic", "*.cls"); // cls/frm/
        mimeTypeMap.put("application/xml", "*.xml"); // cls/frm/
        mimeTypeMap.put("application/andrew-inset", "*.ez");
        mimeTypeMap.put("application/applixware", "*.aw");
        mimeTypeMap.put("application/atom+xml", "*.atom");
        mimeTypeMap.put("application/atomcat+xml", "*.atomcat");
        mimeTypeMap.put("application/atomsvc+xml", "*.atomsvc");
        mimeTypeMap.put("application/bizagi-modeler", "*.bpm");
        mimeTypeMap.put("application/ccxml+xml", "*.ccxml");
        mimeTypeMap.put("application/cu-seeme", "*.cu");
        mimeTypeMap.put("application/davmount+xml", "*.davmount");
        mimeTypeMap.put("application/dita+xml;format=map", "*.ditamap");
        mimeTypeMap.put("application/dita+xml;format=topic", "*.dita");
        mimeTypeMap.put("application/dita+xml;format=val", "*.ditaval");
        mimeTypeMap.put("application/ecmascript", "*.ecma");
        mimeTypeMap.put("application/emma+xml", "*.emma");
        mimeTypeMap.put("application/epub+zip", "*.epub");
        mimeTypeMap.put("application/fits", "*.fits");// fits/fit/fts
        mimeTypeMap.put("application/font-tdpfr", "*.pfr");
        mimeTypeMap.put("application/hyperstudio", "*.stk");
        mimeTypeMap.put("application/illustrator", "*.ai");
        mimeTypeMap.put("application/java-archive", "*.jar");
        mimeTypeMap.put("application/vnd.android.package-archive", "*.apk");
        mimeTypeMap.put("application/x-tika-java-enterprise-archive", "*.ear");
        mimeTypeMap.put("application/x-tika-java-web-archive", "*.war");
        mimeTypeMap.put("application/java-serialized-object", "*.ser");
        mimeTypeMap.put("application/json", "*.json");
        mimeTypeMap.put("application/lost+xml", "*.lostxml");
        mimeTypeMap.put("application/mac-binhex40", "*.hqx");
        mimeTypeMap.put("application/mac-compactpro", "*.cpt");
        mimeTypeMap.put("application/marc", "*.mrc");
        mimeTypeMap.put("application/mathematica", "*.ma");// ma/nb/mb
        mimeTypeMap.put("application/mathml+xml", "*.mathml");
        mimeTypeMap.put("application/mediaservercontrol+xml", "*.mscml");
        mimeTypeMap.put("application/mp4", "*.mp4");// mp4/mp4s
        mimeTypeMap.put("application/mxf", "*.mxf");
        mimeTypeMap.put("application/oebps-package+xml", "*.opf");
        mimeTypeMap.put("application/ogg", "*.ogx");
        mimeTypeMap.put("application/onenote", "*.onetoc"); // onetoc/onetoc2/onetmp/onepkg
        mimeTypeMap.put("application/patch-ops-error+xml", "*.xer");
        mimeTypeMap.put("application/pgp-encrypted", "*.pgp");
        mimeTypeMap.put("application/pgp-signature", "*.asc"); // asc/sig
        mimeTypeMap.put("application/pics-rules", "*.prf");
        mimeTypeMap.put("application/pkix-cert", "*.cer");
        mimeTypeMap.put("application/pkix-pkipath", "*.pkipath");
        mimeTypeMap.put("application/pls+xml", "*.pls");
        mimeTypeMap.put("application/prs.cww", "*.cww");
        mimeTypeMap.put("application/rdf+xml", "*.rdf");// rdf/owl/xmp
        mimeTypeMap.put("application/reginfo+xml", "*.rif");
        mimeTypeMap.put("application/relax-ng-compact-syntax", "*.rnc");
        mimeTypeMap.put("application/resource-lists+xml", "*.rl");
        mimeTypeMap.put("application/resource-lists-diff+xml", "*.rld");
        mimeTypeMap.put("application/rls-services+xml", "*.rs");
        mimeTypeMap.put("application/rsd+xml", "*.rsd");
        mimeTypeMap.put("application/rss+xml", "*.rss");
        mimeTypeMap.put("application/sbml+xml", "*.sbml");
        mimeTypeMap.put("application/scvp-cv-request", "*.scq");
        mimeTypeMap.put("application/scvp-cv-response", "*.scs");
        mimeTypeMap.put("application/scvp-vp-request", "*.spq");
        mimeTypeMap.put("application/scvp-vp-response", "*.spp");
        mimeTypeMap.put("application/sdp", "*.sdp");
        mimeTypeMap.put("application/shf+xml", "*.shf");
        mimeTypeMap.put("application/smil", "*.smi"); // smi/smil/sml
        mimeTypeMap.put("application/sparql-query", "*.rq");
        mimeTypeMap.put("application/sparql-results+xml", "*.srx");
        mimeTypeMap.put("application/srgs", "*.gram");
    }*/

    public String getFileTypByMimeType(String mimeType)
    {
        return null != mimeTypeMap.get(mimeType) ? mimeTypeMap.get(mimeType) : "";
    }



}
