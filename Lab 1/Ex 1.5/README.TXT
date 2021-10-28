Existem 3 estruturas de dados redis.

Um set, Users, destinado a guardar todos os nomes de utilizadores.

Um conjunto de sets que seguem o seguinte padrão: User:name:Followers, em que "name" é cada utilizador. Cada um destes sets guarda os nomes dos utilizadores que seguem o utilizador denominado por "name".

Um conjunto de listas que seguem o seguinte padrão: User:name:MsgQueue, em que "name" é cada utilizador. Cada uma destas listas guarda a lista de mensagens não lidas pelo utilizador denominado por "name".
