namespace java com.spinfosec.parse.service.base.thrift
namespace py ThriftInterface.Parse.Service
namespace cpp parse

include "ParseStruct.thrift"

# 解析服务
service ParseService
{
    # 文件类型识别
    ParseStruct.DetectResult detect(1:ParseStruct.DetectParams detectParams);

    # 解析接口
    ParseStruct.ParseResult parse(1:ParseStruct.ParseParams parseParams);

    # 抽取内嵌文件结构
    ParseStruct.ExtractResult extract(1:ParseStruct.ExtractParams extractParams);
}