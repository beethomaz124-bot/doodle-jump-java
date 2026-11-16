# 🎮 Doodle Jump - Java Swing

![Java](https://img.shields.io/badge/Java-JDK%208%2B-orange?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing%20%2F%20AWT-blue)
![Status](https://img.shields.io/badge/status-concluído-brightgreen)

Um jogo de plataforma vertical inspirado no clássico **Doodle Jump**, desenvolvido em **Java** com **Swing** e aplicando conceitos de **Programação Orientada a Objetos (POO)**.

---

## 🎮 Funcionalidades

- **Menu principal:** tela inicial com botões e transição entre os estados do jogo (**Menu**, **Jogando** e **Game Over**).
- **Sistema de pontuação:** a pontuação aumenta conforme o personagem sobe.
- **Diferentes tipos de plataformas:**
  - 🟢 **Verdes:** plataformas normais que impulsionam o personagem.
  - 🟤 **Marrons:** racham e quebram quando o jogador pisa nelas.
  - 🔩 **Com mola:** permitem saltos mais altos.
- **Inimigos:** monstros que se movimentam pela tela. É possível derrotá-los pulando em cima deles, mas encostar pelos lados termina a partida.
- **Câmera dinâmica:** a tela acompanha o personagem conforme ele sobe, criando a sensação de um cenário infinito.

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| Java (JDK 8+) | Linguagem principal |
| Java Swing e AWT | Janela, painéis e componentes |
| `Graphics2D` | Todos os desenhos do jogo |
| `javax.swing.Timer` | Loop de atualização do jogo |
| `KeyListener` e `ActionListener` | Controles e eventos |

---

## 📁 Estrutura do projeto

| Arquivo | Responsabilidade |
|---|---|
| `Main.java` | Ponto de entrada; inicia o jogo. |
| `GameFrame.java` | Cria e configura a janela principal. |
| `GamePanel.java` | Controla a lógica, a física, os estados e a renderização. |
| `Player.java` | Gerencia o personagem, seus movimentos e pulos. |
| `Platform.java` | Controla as plataformas e seus diferentes tipos. |
| `Monster.java` | Controla os inimigos, seus movimentos e colisões. |

---

## ▶️ Como executar

### Pré-requisitos

- **JDK 8 ou superior** instalado. Para conferir, rode `java -version` e `javac -version`.

### Pelo terminal

1. Abra o terminal na pasta do projeto.
2. Compile os arquivos:

   ```bash
   javac -encoding UTF-8 *.java
   ```

3. Execute o jogo:

   ```bash
   java Main
   ```

### Usando os scripts

| Sistema | Comando |
|---|---|
| Windows | Execute o arquivo `rodar.bat` |
| Linux / macOS | `chmod +x rodar.sh` (só na primeira vez) e depois `./rodar.sh` |

### Usando uma IDE

Abra o projeto no **IntelliJ IDEA**, **Eclipse**, **NetBeans** ou **VS Code** e execute a classe `Main.java`.

---

## ⌨️ Controles

| Tecla | Ação |
|:---:|---|
| ⬅️ Seta esquerda | Move o personagem para a esquerda |
| ➡️ Seta direita | Move o personagem para a direita |

> 💡 O personagem pula automaticamente ao tocar em uma plataforma. Use as setas para guiá-lo até a próxima.

---

## 🧠 Conceitos praticados

- **POO:** separação em classes, encapsulamento e responsabilidades bem definidas.
- **Game loop:** atualização e redesenho periódicos com `Timer`.
- **Máquina de estados:** Menu → Jogando → Game Over.
- **Física simples:** gravidade, velocidade vertical e impulso de salto.
- **Detecção de colisão:** jogador × plataformas e jogador × monstros.
- **Câmera com rolagem vertical** e geração contínua de plataformas.
- **Renderização customizada** com `Graphics2D`.

---

## 💡 Sobre o projeto

Este projeto foi desenvolvido para praticar Java, principalmente POO, interfaces gráficas com Swing, movimentação de personagens, colisões e lógica de jogos.

Foi uma experiência divertida transformar a ideia de um jogo clássico em uma versão própria, explorando os recursos da linguagem.

---
