namespace java com.spinfosec.encryptAnalyze.thrift.service
namespace py ThriftInterface.CiphertextRecognition.Service

service CiphertextRecognitionService
{
    map<string, double> ciphertextRecognition(1:list<string> ciphertext);
}