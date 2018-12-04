# coding: utf-8
import sys

from thrift.Thrift import TException

sys.path.append('DORIF')
from thrift.transport import TSocket
from thrift.protocol import TCompactProtocol
from DORIF import *

transport = None
client = None
def exception_handler(exception_type, exception, traceback):
    print("%s: %s", exception_type.__name__, exception)

def user_mode(IP,porta):
    try:
        global transport
        transport = TSocket.TSocket(IP, porta)
        protocol = TCompactProtocol.TCompactProtocol(transport)
        global client
        client = DORIFService.Client(protocol)
        transport.open()
        print("Conectado com o Sucesso a", IP, ":", porta)
    except:
        print("Falha na conexão com o servidor, favor verificar se o IP e a porta estão corretos!")
        exit(0)
    cases = {
        "ping":ping,
        "sendself":sendself,
        "addTerm":addTerm,
        "getTerm":getTerm,
        "startCollecting":startCollecting,
        "stopCollecting":stopCollecting,
        "getServerNumDocs":getServerNumDocs,
        "getTotalNumDocs":getTotalNumDocs,
        "containsTermo":containsTermo,
        "getTotalTermos":getTotalTermos,
        "GlobalTerms":getTotalGlobalTermsInfo,
        "DocTerms":getTotalDocTermsInfo,
        "DocsInfo":getTotalDocInfo,
        "query":query
    }
    while(True):
        entrada = input("@-").split();
        try:
            cases[entrada[0]](entrada[1:])
        except KeyError:
            print("Comando Inválido!")
def main():
    print("SimplePythonClient\n")
    try:
        IP = sys.argv[1]
        porta = int(sys.argv[2])
    except:
        print("Favor fornecer IP e Porta na chamada do programa Main.py")
        IP = input("Digite o IP: ")
        porta = input("Digite a porta: ")
    user_mode(IP,porta)
def ping(args):
    global client
    try:
        print("Pingando servidor...")
        a = client.ping()
        print("Server Pinged: "+str(a))
    except TException as te:
        print("Falha ao pingar o servidor "+te.message)
def sendself(args):
    global  client
    try:
        Node = client.send_self()
        print(Node.name)
    except:
        print("Falha ao Receber Node")
def addTerm(args):
    try:
        client.addTermo(args[0])
        print("Termo "+args[0]+" added to server")
    except TException as te:
        print("Falha ao adicionar termo. " + te.message)
    except Exception as e:
        print("Falha ao adicionar termo. "+e.message)
def getTerm(args):
    try:
        termo = client.getTermo(args[0])
        print(termo.term+", frequencia: "+str(termo.frequency))
    except DORIFService.TermNotFoundException as tnf:
        print("Termo não encontrado!")
    except TException as te:
        print(te.message)
def startCollecting(args):
    try:
        print("Starting request for handler to start collecting")
        client.startCollecting()
        print("Requested handler to start collecting")
    except TException as te:
        print(te.message)
def stopCollecting(args):
    try:
        print("Starting request for handler to stop collecting")
        client.stopCollecting()
        print("Requested handler to stop collecting")
    except TException as te:
        print(te.message)
def getServerNumDocs(args):
    try:
        print("Server's Document Count: "+str(client.getServerNumDocs()))
    except TException as te:
        print(te.message)
def getTotalNumDocs(args):
    try:
        print("Total Document Count: "+str(client.getTotalNumDocs()))
    except TException as te:
        print(te.message)
def containsTermo(args):
    try:
        contains = client.containsTermo(args[0])
        if(contains):
            print("Termo existe no vocabulário")
        else:
            print("Termo não existe no vocabulário")
    except TException as te:
        print(te.message)
def getTotalTermos(args):
    try:
        print("Requerindo todos os termos do servidor")
        termos = client.getTotalGenericTerms()
        for termo in termos:
            print(termo.term+" "+str(termo.frequency))
    except TException as te:
        print(te.message)
def getTotalGlobalTermsInfo(args):
    try:
        print("Recuperando informações sobre todos os termos de vocabulário geral")
        termos = client.getTotalGlobalTermsInfo()
        print('{:^4}'.format("ID")+"|"+'{:^12}'.format("TERMO")+"|"+'{:^12}'.format("FREQUENCIA")+"|"+'{:^7}'.format("IDF")+"|")
        for termo in termos:
            print('{:^4}'.format(str(termo.id))+"|"+'{:^12}'.format(termo.term)+"|"+'{:^12}'.format(str(termo.frequency))+"|"+'{:^7}'.format(("%.3f" % termo.idf))+"|")
    except TException as te:
        print(te.message)
def getTotalDocTermsInfo(args):
    try:
        print("Recuperando informações sobre todos os termos de vocabulário dos documentos")
        termos = client.getTotalDocTermsInfo()
        print('{:^22}'.format("DOCUMENTO")+"|"+'{:^5}'.format("ID")+"|"+'{:^12}'.format("TERMO")+"|"+'{:^12}'.format("FREQUENCIA")+"|"+'{:^7}'.format("TF")+"|"+'{:^7}'.format("TFIDF")+"|")
        for termo in termos:
            print('{:^22}'.format(termo.docName)+"|"+'{:^5}'.format(str(termo.id))+"|"+'{:^12}'.format(termo.term)+"|"+'{:^12}'.format(str(termo.frequency))+"|"
                  +'{:^7}'.format(("%.3f" % termo.tf))+"|"+'{:^7}'.format(("%.3f" % termo.tfidf))+"|")
    except TException as te:
        print(te.message)
def getTotalDocInfo(args):
    try:
        print("Recuperando informações de todos os documentos....")
        documents = client.getTotalDocInfo()
        print('{:^19}'.format("SERVIDOR")+"|"+'{:^5}'.format("ID")+"|"+'{:^22}'.format("NOME")+"|"+'{:^23}'.format("CAMINHO")+"|"+'{:^7}'.format("NORMA")+"|")
        for doc in documents:
            print('{:^19}'.format(doc.serverName+"("+str(doc.serverId)+")")+"|"+'{:^5}'.format(str(doc.id))+"|"+'{:^22}'.format(doc.name)+"|"+'{:^23}'.format(doc.path)
                  +"|"+'{:^7}'.format(("%.3f" % doc.norma))+"|")
    except TException as te:
        print(te.message)
def query(args):
    query = ""
    try:
        query = ' '.join(args[0:])
    except:
        print("Nenhuma consulta fornecida")
        return
    try:
        print("Consultando servidor para "+query+"...")
        docs = client.query(query)
        print("|"+'{:^7}'.format("SCORE")+"|"+'{:^22}'.format("NOME")+"|"+'{:^5}'.format("ID")+"|"+'{:^11}'.format("DESCRICAO")+"|"+'{:^19}'.format("SERVER ID")+"|"+'{:^19}'.format("SERVER NAME")+"|")
        for doc in docs:
            print("|"+'{:^7}'.format(("%.3f" % doc.score))+"|"+'{:^22}'.format(doc.name)+"|"+'{:^5}'.format(str(doc.id))+"|"+'{:^11}'.format(doc.desc)+"|"+'{:^19}'.format(str(doc.serverId))
                  +"|"+'{:^19}'.format(doc.serverName)+"|")
    except TException as te:
        print(te.message)
main()