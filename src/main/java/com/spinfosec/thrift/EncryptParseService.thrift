namespace java com.spinfosec.thrift.service
namespace py ThriftInterface.EncryptParse.Service

include "EncryptParseStruct.thrift"

service EncryptParseService
{
    EncryptParseStruct.Result parseFile(1:EncryptParseStruct.TransforDataInfo transforDataInfo);

    bool testConnect();
}