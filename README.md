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
- Lógica de xadrez implementada em classes próprias
- Timer com javax.swing.Timer
- IA baseada em Minimax com avaliação de posição
- Controle de profundidade para IA

## Estrutura do Projeto
ChessGUI/
├── resources/                     # Ícones das peças
│   ├── bB.png  bK.png  bN.png
│   ├── bP.png  bQ.png  bR.png
│   ├── wB.png  wK.png  wN.png
│   ├── wP.png  wQ.png  wR.png
├── src/
│   ├── controller/
│   │   └── Game.java              # Lógica de regras e movimentação
│   ├── model/
│   │   ├── board/
│   │   │   ├── Board.java
│   │   │   ├── Move.java
│   │   │   └── Position.java
│   │   └── pieces/
│   │       ├── Bishop.java
│   │       ├── King.java
│   │       ├── Knight.java
│   │       ├── Pawn.java
│   │       ├── Piece.java
│   │       ├── Queen.java
│   │       └── Rook.java
│   └── view/
│       └── ChessGUI.java          # Interface gráfica principal
└── README.md

## Como Executar

Clone o projeto:

git clone <URL_DO_REPOSITORIO>

Compile os arquivos Java:

javac -d bin src/**/*.java

Execute o jogo:

java -cp bin view.ChessGUI

Certifique-se de que a pasta resources/ esteja no mesmo nível de execução para carregar os ícones das peças corretamente.

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