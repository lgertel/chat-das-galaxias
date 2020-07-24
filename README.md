Dependências principais:
 - Axon (Spring Boot starter)
 - Spring Data JPA
 - Freemarker
 - Web 
 - Reactor
 - H2 (Embedded Database)
 - Spring Boot Test
 - Axon Test

### Layout do aplicativo ###

A lógica do aplicativo é dividida entre vários pacotes.

- `com.arquitetodasgalaxias.chat`
  O pacote principal. Contém a classe Application com a configuração.
- `com.arquitetodasgalaxias.chat.commandmodel`
  Contém o modelo de comando. 
- `com.arquitetodasgalaxias.chat.coreapi`
  O chamado * core api *. É aqui que colocamos os Comandos, Eventos e Consultas.
  Como comandos, eventos e consultas são imutáveis, usamos o Kotlin para defini-los. Kotlin permite que você
  definir de forma concisa cada evento, comando e consulta em uma única linha.
- `com.arquitetodasgalaxias.chat.query.rooms.messages`
  Contém as projeções (também chamadas de modelo de exibição ou modelo de consulta) para as mensagens que foram transmitidas em um
  sala específica. Este pacote contém os manipuladores de eventos para atualizar as projeções,
  bem como os manipuladores de consulta para ler os dados.
- `com.arquitetodasgalaxias.chat.query.rooms.participants`
  Contém a projeção para servir a lista de participantes em uma determinada sala de bate-papo.
- `com.arquitetodasgalaxias.chat.query.rooms.summary`
  Contém a Projeção para servir uma lista de salas de bate-papo disponíveis e o número de participantes.
- `com.arquitetodasgalaxias.chat.restapi`
  Esta é a API de comando e consulta REST para alterar e ler o estado do aplicativo.
  As chamadas de API aqui são convertidas em comandos e consultas para o aplicativo processar.

### Interface do usuário do Swagger ###
A aplicação tem o 'Swagger' ativado. Você pode usar o Swagger para enviar solicitações.

Visite: [http: // localhost: 8080 / swagger-ui.html] (http: // localhost: 8080 / swagger-ui.html)

<b> Nota </b>: a interface do usuário do Swagger não suporta a 'Consulta de assinatura' mais adiante na atribuição,
 como o Swagger não suporta resultados de streaming.
É recomendável emitir uma operação regular `curl ', ou algo nesse sentido, para verificar a Consulta de Assinatura.


### H2 Console ###
O aplicativo tem o 'H2 Console' configurado, para que você possa espiar o conteúdo do banco de dados.

Visita: [http: // localhost: 8080 / h2-console] (http: // localhost: 8080 / h2-console)
Digite a URL JDBC: `jdbc:h2:tcp://localhost:9092/mem:chatapp`
Deixe outros valores com os padrões e clique em 'conectar'.

Preparação
-----------

O Axon Framework funciona melhor com o AxonServer e, neste projeto de exemplo, assumimos que você o está usando.
O AxonServer precisa ser baixado separadamente.
Você pode executar o AxonServer como um contêiner de docker executando:
`` `script de shell
docker run -d -p 8024: 8024 -p 8124: 8124 -p 8224: 8224 --name axonserver axoniq / axonserver
`` ``