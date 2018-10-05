Prova P1 Engenharia de Software 3
-------------------------------------------------------------------------------
    Esse projeto temo como objetivo solucionar o problema proposto da prova par
da P1 de Engenharia de Software 3.

Getting Started
-------------------------------------------------------------------------------
* Pré-requisitos : Ter um C9 ou qualquer IDE com Scala e Akka
                   Copiar o diretorio "matrix"
                   Copiar o diretorio que deseja fazer a execução(sequential ou parallel-local)
                  
Sequential
------------------------------------------------------------------------------- 
    Caso escolha o diretorio sequential basta inicializar o processo, se houver
o desejo de mudar as matrizes dos calculos mude no codigo indicado a baixo.
        
        val matrixA = Source.fromFile("nova localização").getLines.toList.map(_.split(" ").map(_.toInt)).transpose
        val matrixB = Source.fromFile("nova localização").getLines.toList.map(_.split(" ").map(_.toInt))

Parallel-local
-------------------------------------------------------------------------------
Master
-------------------------------------------------------------------------------                    
    Inicia a aplicação enviando uma mensagem Read, que contem duas String in-
dicando a localização das matrizes.

        Read(pathA : String, pathB : String)

Reader
------------------------------------------------------------------------------- 
    Recebe a mensagem Read, com isso executa a leitura das matrizes e envia uma
mensagem Calculate, contendo uma tupla de inteiros que são os indices, um array
de String e uma String.

        Calculate(coordinate: (Int, Int), a : Array[String], b : String)
        
Worker
-------------------------------------------------------------------------------         
    Recebe a mensagem Calculate, depois de seu recebimento executa o calculo
matricial C = B ∗ (A + BT), aonde "T" significa matrix transposta de "B", apos
o calulo é enviado a mensagem Write, cujo seu conteudo é uma tupla de inteiros
sendo o indice do numero calculado e um inteiro com o valor do item.

        Write(coordinate: (Int, Int), item : Int)
Writer
------------------------------------------------------------------------------- 
    Recebe a mensagem Write e escreve corretamente o numero calculado pelo Worker
em um arquivo chamado "result" e tambem escreve em um arquivo "status" a quantidade
de numeros positivos, negativo e nulos presente na matrix C.

Built With
------------------------------------------------------------------------------- 
    * SBT - Scala Build Tool