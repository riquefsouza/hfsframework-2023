docker pull ibmcom/db2

docker run -itd --name db2docker --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=abcd1234 -e DBNAME=banco -v db2-volume:/database ibmcom/db2

docker exec -ti db2docker bash -c "su - db2inst1"

ou

docker exec -it db2docker /bin/bash
su db2inst1

db2 create database hefesto
db2 connect to hefesto




grant createtab,bindadd,connect on database to user db2inst1;
connect reset;
