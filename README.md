ChessGUI – Jogo de Xadrez com IA

Autor: Gabriel Ozório Franco

Descrição:
ChessGUI é um jogo de xadrez desenvolvido em Java Swing, com inteligência artificial que pode jogar como Brancas, Pretas ou contra si mesma. O projeto possui interface gráfica completa, painel de peças capturadas, histórico de lances, controle de tempo e níveis de dificuldade ajustáveis.

## Funcionalidades

🎮 Jogar contra a IA ou entre humanos.

🤖 IA configurável para jogar como Brancas, Pretas ou ambos os lados.

🕹️ **Seleção de nível de dificuldade da IA (1 a 4):**
- O usuário pode escolher o nível de profundidade da IA para cada lado, tornando o jogo mais fácil ou desafiador.

🖼️ **Painel lateral de peças capturadas:**
- As peças capturadas por cada lado são exibidas em painéis laterais, facilitando o acompanhamento do material.

⏱️ **Timer por jogador, com destaque para o jogador da vez:**
- Cada jogador possui um cronômetro individual. O tempo do jogador da vez aparece destacado em vermelho para facilitar a visualização.

🎨 **Mudança de cores das casas do tabuleiro:**
- O tabuleiro possui casas claras e escuras personalizadas, tornando a interface mais agradável.

🔄 **Histórico de lances colorido: azul (Brancas) e vermelho (Pretas):**
- Os lances são registrados com cores diferentes para cada lado, facilitando o acompanhamento da partida.

♟️ Promoção de peões com escolha de peça.

⬅️ Desfazer movimentos.

🆕 Novo jogo e reinício rápido.

---

## Tecnologias Utilizadas

- Java 17+
- Java Swing (GUI)

## Estrutura do Projeto

ChessGUI/
├── .vscode/
├── bin/ 
│ ├── controller/ 
│ ├── model/ 
│ ├── view/ 
│ └── resources/ 
├── src/ 
│ ├── controller/
│ ├── model/
│ └── view/
└── README.md 


## Como Compilar e Executar (Windows PowerShell)

```powershell
cd "CAMINHO/DO/PROJETO"   # entra na pasta do projeto
mkdir bin                 # cria a pasta bin
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })   # compila os .java
java -cp bin view.ChessGUI   # executa o jogo

## Uso

- **Selecionar Peça:** clique na peça desejada.
- **Movimento Legal:** quadrados verdes indicam movimentos possíveis.
- **Último Lance:** quadrados amarelos.
- **Peças Capturadas:** mostradas no painel lateral.
- **Histórico de Lances:** cores azul (Brancas) e vermelho (Pretas).
- **Promoção de Peão:** escolha entre Rainha, Torre, Bispo ou Cavalo ao chegar na última linha.
- **IA Níveis:** ajuste a profundidade de decisão da IA de 1 a 4.
- **Tempo do Jogador:** cronômetro individual, com destaque em vermelho para o jogador da vez.

---

Autor

Gabriel Ozório Franco