namespace java com.spinfosec.parse.bean
namespace py ThriftInterface.Parse.Struct
namespace cpp parse

# 文件类型识别参数
struct DetectParams
{
    1:binary stream;                # 被识别文件流
    2:string fileName;              # 文件名称
}

# 文件类型识别结果
struct DetectResult
{
    1:string type;                  # 文件类型
    2:bool success;                 # 响应
    3:string message;               # 响应消息
}

# 请求接收参数
struct ParseParams
{
    1:binary stream;                # 被解析文件流
    2:string fileName;              # 文件名称
    3:bool enableOCR = true;        # 默认开启OCR
    4:bool enableOfficeOCR = true;  # 默认开启Office OCR
    5:bool enablePdfOCR = true;     # 默认开启PDF OCR
}

# 文件单元
struct ResultItem
{
    1:string mimeType;              # MIME
    2:string charset;               # 文本文件编码
    3:i32 length;                   # 解析结果字节长度
    4:string content;               # 解析结果字符串
    5:string fileName;              # 文件名称（包含内嵌元素的文件名等）
    6:i32 headersLength;            # 页眉字节长度
    7:string headers;               # 页眉解析字符串
    8:i32 footersLength;            # 页脚字节长度
    9:string footers;               # 页脚解析字符串
    10:map<string, string> metadatas;   # 元数据属性信息
    11:i32 code;                    # 单元响应状态码
    12:string message;              # 单元响应消息
}

# 解析服务处理结果
struct ParseResult
{
    1:list<ResultItem> items;       # 文件单元集合
    2:i32 code;                     # 响应状态码
    3:string message;               # 响应消息
}

# 文件抽取接口参数
struct ExtractParams
{
    1:binary stream;                # 被抽取文件流
    2:string fileName;              # 文件名称
}

struct ExtractResult
{
    1:list<ExtractResultItem> items;# 文件单元集合
    2:i32 code;                     # 响应状态码
    3:string message;               # 响应消息
}

struct ExtractResultItem
{
    1:string fileName;              # 文件名称（包含内嵌元素的文件名等）
    2:binary stream;                # 被抽取文件流
    3:i32 code;                     # 单元响应状态码
    4:string message;               # 单元响应消息
}