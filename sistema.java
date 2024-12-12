
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/// Classe que representa o nó das árvores BST e AVL
class NoArvore {
  String key; // codigo da escola como chave
  String der;
  String municipio;
  String descricao;
  String serie;
  int grau;
  String escola;
  int classes;
  int alunos;

  NoArvore left, right;

  // Contrutor que inicializa o nó com os dados do banco de dados
  public NoArvore(String key, String der, String municipio, String descricao, String serie, int grau, String escola,
      int classes, int alunos) {
    this.left = this.right = null;
    this.key = key;
    this.der = der;
    this.municipio = municipio;
    this.descricao = descricao;
    this.serie = serie;
    this.grau = grau;
    this.escola = escola;
    this.classes = classes;
    this.alunos = alunos;
  }
}

class ArvoreBST {
  NoArvore raiz;
  int comparacoes = 0;

  // Função Inserir na árvore BST
  public void inserir(String key, String der, String municipio, String descricao, String serie, int grau,
      String escola, int classes, int alunos) {
    raiz = inserirRecursivo(raiz, key, der, municipio, descricao, serie, grau, escola, classes, alunos);
  }

  // Método recursivo
  private NoArvore inserirRecursivo(NoArvore raiz, String key, String der, String municipio, String descricao,
      String serie, int grau, String escola, int classes, int alunos) {
    if (raiz == null) {
      return new NoArvore(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    }

    comparacoes++; // Incrementa o contador de comparações para análise de desempenho

    // Verifica a posição correta do novo vó comparando com as chaves
    if (key.compareTo(raiz.key) < 0) {
      raiz.left = inserirRecursivo(raiz.left, key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    } else if (key.compareTo(raiz.key) > 0) {
      raiz.right = inserirRecursivo(raiz.right, key, der, municipio, descricao, serie, grau, escola, classes,
          alunos);
    }
    return raiz;
  }

  // Função Buscar na árvore AVL
  public NoArvore buscar(String key) {
    return buscarRecursivo(raiz, key);
  }

  // Método recursivo
  private NoArvore buscarRecursivo(NoArvore raiz, String key) {
    if (raiz == null || raiz.key.equals(key)) {
      return raiz;
    }
    if (key.compareTo(raiz.key) < 0) {
      return buscarRecursivo(raiz.left, key);
    }
    return buscarRecursivo(raiz.right, key);
  }

  // Função Remover na árvore BST (Sem balanceamento)
  public void remover(String key) {
    raiz = removerRecursivo(raiz, key);
  }

  // Método recursivo
  private NoArvore removerRecursivo(NoArvore raiz, String key) {
    if (raiz == null) {
      return raiz;
    }

    if (key.compareTo(raiz.key) < 0) {
      raiz.left = removerRecursivo(raiz.left, key);
    } else if (key.compareTo(raiz.key) > 0) {
      raiz.right = removerRecursivo(raiz.right, key);
    } else {
      // Se o nó tiver um filho ou nenhum
      if (raiz.left == null) {
        return raiz.right;
      }
      if (raiz.right == null) {
        return raiz.left;
      }

      // Se o nó tiver dois filhos, encontra o menor valor da subárvore direita
      raiz.key = encontrarMin(raiz.right);
      raiz.right = removerRecursivo(raiz.right, raiz.key);
    }
    return raiz;
  }

  // Função auxiliar da função Remover da árvore BST
  private String encontrarMin(NoArvore raiz) {
    String minValor = raiz.key;
    while (raiz.left != null) {
      raiz = raiz.left;
      minValor = raiz.key;
    }
    return minValor;
  }

  // Método para contar alunos por tipo de deficiência
  public int contarAlunosPorDeficiencia(String descricao) {
    return contarAlunosPorDeficienciaRecursivo(raiz, descricao);
  }

  // Função recursiva para percorrer toda a árvore
  private int contarAlunosPorDeficienciaRecursivo(NoArvore atual, String descricao) {
    if (atual == null) {
      return 0; // Caso base: árvore vazia
    }

    int count = 0;
    // Verifica se o tipo de deficiência corresponde e soma os alunos
    if (atual.descricao.equals(descricao)) {
      count += atual.alunos;
    }

    // Chama recursivamente para as subárvores esquerda e direita
    count += contarAlunosPorDeficienciaRecursivo(atual.left, descricao);
    count += contarAlunosPorDeficienciaRecursivo(atual.right, descricao);

    return count;
  }

  // Método para buscar a escola com mais alunos
  public NoArvore buscarEscolaComMaisAlunos() {
    return buscarRecursivoMaiorAlunos(raiz, null);
  }

  private NoArvore buscarRecursivoMaiorAlunos(NoArvore atual, NoArvore maior) {
    if (atual == null) {
      return maior;
    }

    // Verifica se o número de alunos do nó atual é maior que o maior encontrado até
    // agora
    if (maior == null || atual.alunos > maior.alunos) {
      maior = atual;
    }

    // Recursivamente busca nas subárvores esquerda e direita
    maior = buscarRecursivoMaiorAlunos(atual.left, maior);
    maior = buscarRecursivoMaiorAlunos(atual.right, maior);

    return maior;
  }

  // Método para contar escolas por município
  public Map<String, Integer> contarEscolasPorMunicipio() {
    Map<String, Integer> escolasPorMunicipio = new HashMap<>();
    contarPorMunicipio(raiz, escolasPorMunicipio);
    return escolasPorMunicipio;
  }

  private void contarPorMunicipio(NoArvore atual, Map<String, Integer> map) {
    if (atual == null)
      return;
    map.put(atual.municipio, map.getOrDefault(atual.municipio, 0) + 1);
    contarPorMunicipio(atual.left, map);
    contarPorMunicipio(atual.right, map);
  }

  // Método para contar alunos por escola
  public Map<String, Integer> contarAlunosPorEscola() {
    Map<String, Integer> alunosPorEscola = new HashMap<>();
    contarPorEscola(raiz, alunosPorEscola);
    return alunosPorEscola;
  }

  private void contarPorEscola(NoArvore atual, Map<String, Integer> map) {
    if (atual == null)
      return;
    map.put(atual.escola, map.getOrDefault(atual.escola, 0) + atual.alunos);
    contarPorEscola(atual.left, map);
    contarPorEscola(atual.right, map);
  }

  // Método para contar escolas com DA / DF / DM / DV
  public int contarEscolasEspeciais() {
    int count = contarEspeciais(raiz, 0);
    System.out.println("Total de escolas especiais encontradas: " + count); // Depuração
    return count;
  }

  private int contarEspeciais(NoArvore atual, int count) {
    if (atual == null) {
      return count; // Base da recursão
    }

    // Padroniza e verifica se a série corresponde a DA, DF, DM ou DV
    String serie = atual.serie.trim().toUpperCase();
    if (serie.equals("EDUCAÇÃO ESPECIAL - DA") || serie.equals("EDUCAÇÃO ESPECIAL - DF")
        || serie.equals("EDUCAÇÃO ESPECIAL - DM") || serie.equals("EDUCAÇÃO ESPECIAL - DV")
        || serie.equals("ESPECTRO DO AUTISMO")
        || serie.equals("EDUCAÇÃO ESPECIAL - ALTAS HABILIDADES/SUPERDOTAÇÃO – SALA DE RECURSO")) {
      System.out.println("Escola especial encontrada: " + atual.escola + " (Série: " + serie + ")"); // Depuração
      count++;
    }

    // Continua percorrendo a árvore
    count = contarEspeciais(atual.left, count);
    count = contarEspeciais(atual.right, count);

    return count;
  }
}
// ---------------------------------------------------------------------------------------------------------------------------------------------

class ArvoreAVL {
  NoArvore raiz;
  int comparacoes = 0;

  // Função Inserir na árvore AVL
  public void inserir(String key, String der, String municipio, String descricao, String serie, int grau,
      String escola, int classes, int alunos) {
    raiz = inserirRecursivo(raiz, key, der, municipio, descricao, serie, grau, escola, classes, alunos);
  }

  // Método recursivo
  private NoArvore inserirRecursivo(NoArvore raiz, String key, String der, String municipio, String descricao,
      String serie, int grau, String escola, int classes, int alunos) {
    if (raiz == null) {
      return new NoArvore(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    }
    comparacoes++;
    if (key.compareTo(raiz.key) < 0) {
      raiz.left = inserirRecursivo(raiz.left, key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    } else if (key.compareTo(raiz.key) > 0) {
      raiz.right = inserirRecursivo(raiz.right, key, der, municipio, descricao, serie, grau, escola, classes,
          alunos);
    }

    // especificidade da AVL
    return balancear(raiz);
  }

  // Função para calcular a altura da árvore para usar no balanceamento
  private int altura(NoArvore raiz) {
    if (raiz == null) {
      return 0;
    }
    return 1 + Math.max(altura(raiz.left), altura(raiz.right));
  }

  // Função que calcula o Fator de Balanceamento
  private int fatorBalanceamento(NoArvore raiz) {
    if (raiz == null) {
      return 0;
    }
    return altura(raiz.left) - altura(raiz.right);
  }

  // rotação a direita
  private NoArvore rotacaoDireita(NoArvore y) {
    if (y == null || y.left == null) { // verifica se o nó é null
      return y;
    }
    NoArvore x = y.left;
    NoArvore T2 = x.right;
    x.right = y;
    y.left = T2;
    return x;
  }

  // rotação a esquerda
  private NoArvore rotacaoEsquerda(NoArvore x) {
    if (x == null || x.right == null) {
      return x;
    }
    NoArvore y = x.right;
    NoArvore T2 = y.left;
    y.left = x;
    x.right = T2;
    return y;
  }

  // rotação dupla a direita
  private NoArvore rotacaoDuplaDireita(NoArvore raiz) {
    if (raiz == null | raiz.right == null) {
      return raiz;
    }
    raiz.right = rotacaoDireita(raiz.right);
    return rotacaoEsquerda(raiz);
  }

  // Rotação dupla a esquerda
  private NoArvore rotacaoDuplaEsqueda(NoArvore raiz) {
    if (raiz == null || raiz.left == null) {
      return raiz;
    }
    raiz.left = rotacaoDireita(raiz.left);
    return rotacaoDireita(raiz);
  }

  // Balanceamento
  private NoArvore balancear(NoArvore raiz) {
    int fb = fatorBalanceamento(raiz);

    if (fb > 1) {
      if (fatorBalanceamento(raiz.left) >= 0) {
        return rotacaoDireita(raiz);
      } else {
        return rotacaoDuplaEsqueda(raiz);
      }
    }
    if (fb < -1) {
      if (fatorBalanceamento(raiz.right) <= 0) {
        return rotacaoEsquerda(raiz);
      } else {
        return rotacaoDuplaDireita(raiz);
      }
    }
    return raiz;
  }

  // Função Buscar na árvore AVL
  public NoArvore buscar(String key) {
    return buscarRecursivo(raiz, key);
  }

  // Método recursivo
  private NoArvore buscarRecursivo(NoArvore raiz, String key) {
    if (raiz == null || raiz.key.equals(key)) {
      return raiz;
    }
    if (key.compareTo(raiz.key) < 0) {
      return buscarRecursivo(raiz.left, key);
    }
    return buscarRecursivo(raiz.right, key);
  }

  // Função Remover na árvore AVL
  public void remover(String key) {
    raiz = removerRecursivo(raiz, key);
  }

  // Método recursivo
  private NoArvore removerRecursivo(NoArvore raiz, String key) {
    if (raiz == null) {
      return raiz;
    }

    if (key.compareTo(raiz.key) < 0) {
      raiz.left = removerRecursivo(raiz.left, key);
    } else if (key.compareTo(raiz.key) > 0) {
      raiz.right = removerRecursivo(raiz.right, key);
    } else {
      if (raiz.left == null) {
        return raiz.right;
      }
      if (raiz.right == null) {
        return raiz.left;
      }
      raiz.key = encontrarMin(raiz.right);
      raiz.right = removerRecursivo(raiz.right, raiz.key);
    }
    return balancear(raiz);
  }

  // Função auxiliar da função remover da árvore AVL
  private String encontrarMin(NoArvore raiz) {
    String minValor = raiz.key;
    while (raiz.left != null) {
      raiz = raiz.left;
      minValor = raiz.key;
    }
    return minValor;
  }

  // Funções para Análise

  // Função para contar a quantidade de comparações de desempenho das árvores BST
  // e AVL
  public int comparacoes() {
    return comparacoes;
  }

  // Função para zerar a quantidade de comparações
  public void resetComparacoes() {
    comparacoes = 0;
  }

  // Função para contar alunos por descrição específica
  int countDA = 0, countDF = 0, countDM = 0, countDV = 0, countAutismo = 0, countSuperdotado = 0;

  // Função para contar a quantidade de alunos especiais
  public void countAlunosPorDescricao(String descricao) {
    countDA = 0;
    countDF = 0;
    countDM = 0;
    countDV = 0;
    countAutismo = 0;
    countSuperdotado = 0;

    countAlunosPorDescricaoRecursivo(raiz, descricao);
  }

  private void countAlunosPorDescricaoRecursivo(NoArvore no, String descricao) {
    if (no == null) {
      return;
    }

    // Verifica se a descrição do nó é a mesma
    if (no.descricao.equals(descricao)) {
      if (descricao.equals("EDUCAÇÃO ESPECIAL - DA")) {
        countDA += no.alunos;
      } else if (descricao.equals("EDUCAÇÃO ESPECIAL - DF")) {
        countDF += no.alunos;
      } else if (descricao.equals("EDUCAÇÃO ESPECIAL - DM")) {
        countDM += no.alunos;
      } else if (descricao.equals("EDUCAÇÃO ESPECIAL - DV")) {
        countDV += no.alunos;
      } else if (descricao.equals("ESPECTRO AUTISTA")) {
        countAutismo += no.alunos;
      } else if (descricao.equals("EDUCAÇÃO ESPECIAL - ALTAS HABILIDADES/SUPERDOTAÇÃO - SALA DE RECURSO")) {
        countSuperdotado += no.alunos;
      }
    }

    countAlunosPorDescricaoRecursivo(no.left, descricao);
    countAlunosPorDescricaoRecursivo(no.right, descricao);
  }
}

public class teste02 {
  public static void main(String[] args) throws FileNotFoundException {
    // lê o arquivo
    File file = new File("VW_ED_ESPECIAL_20190517_1.csv");
    Scanner scanner = new Scanner(file);
    ArvoreBST bst = new ArvoreBST();
    ArvoreAVL avl = new ArvoreAVL();

    // pula a primeira linha
    scanner.nextLine();

    // inserindo dados na BST
    while (scanner.hasNextLine()) {
      String linha = scanner.nextLine().trim();
      String[] dados = linha.split(";");

      String der = dados[0];
      String municipio = dados[1];
      String descricao = dados[2];
      String serie = dados[3];
      int grau = Integer.parseInt(dados[4]);
      String key = dados[5];
      String escola = dados[6];
      int classes = Integer.parseInt(dados[7]);
      int alunos = Integer.parseInt(dados[8]);

      bst.inserir(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    }

    // Reiniciando o scanner
    scanner.close();
    scanner = new Scanner(file);

    // Pula a primeira linha
    scanner.nextLine();

    // inserindo dados na AVL
    while (scanner.hasNextLine()) {
      String linha = scanner.nextLine().trim();
      String[] dados = linha.split(";");

      String der = dados[0];
      String municipio = dados[1];
      String descricao = dados[2];
      String serie = dados[3];
      int grau = Integer.parseInt(dados[4]);
      String key = dados[5];
      String escola = dados[6];
      int classes = Integer.parseInt(dados[7]);
      int alunos = Integer.parseInt(dados[8]);

      avl.inserir(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
    }

    while (true) {
      // limpar o scanner
      scanner.close();

      // Menu
      System.out
          .println("\n[1] - inserir novos dados\n[2] - Buscar dado\n[3] - Remover dado\n[4] - Analise\n[5] - Sair\n");
      Scanner input = new Scanner(System.in);
      System.out.println("Digite a sua opcao: ");
      int opc = input.nextInt();

      switch (opc) {

        case 1:
          input.nextLine();
          System.out.println("Digite o codigo da escola: ");
          String key = input.nextLine().toUpperCase();
          System.out.println("Digite a Diretoria de Ensino Regional (DER): ");
          String der = input.nextLine().toUpperCase();
          System.out.println("Digite o município da escola: ");
          String municipio = input.nextLine().toUpperCase();
          System.out.println("Digite a descrição: ");
          String descricao = input.nextLine().toUpperCase();
          System.out.println("Digite a série: ");
          String serie = input.nextLine().toUpperCase();
          System.out.println("Digite o nome da escola: ");
          String escola = input.nextLine().toUpperCase();
          System.out.println("Digite o grau (número inteiro): ");
          int grau = input.nextInt();
          System.out.println("Digite o número de classes (número inteiro): ");
          int classes = input.nextInt();
          System.out.println("Digite o número de alunos (número inteiro): ");
          int alunos = input.nextInt();
          bst.inserir(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
          avl.inserir(key, der, municipio, descricao, serie, grau, escola, classes, alunos);
          break;

        case 2:
          input.nextLine();
          System.out.println("Digite o codigo da escola que vai ser buscado: ");
          String nomeEscola = input.nextLine().toUpperCase();

          // Busca na árvore BST
          NoArvore resultadoBST = bst.buscar(nomeEscola);
          if (resultadoBST != null) {
            System.out.println("\nValor encontrado na árvore BST:");
            System.out.println("---------------------------------------");
            System.out.println("Escola: " + resultadoBST.key);
            System.out.println("DER: " + resultadoBST.der);
            System.out.println("Município: " + resultadoBST.municipio);
            System.out.println("Descrição: " + resultadoBST.descricao);
            System.out.println("Série: " + resultadoBST.serie);
            System.out.println("Grau: " + resultadoBST.grau);
            System.out.println("Escola: " + resultadoBST.escola);
            System.out.println("Classes: " + resultadoBST.classes);
            System.out.println("Alunos: " + resultadoBST.alunos);
          } else {
            System.out.println("\nValor não encontrado na árvore BST\n");
          }
          break;
        case 3:
          input.nextLine();
          System.out.println("Digite o nome da escola a ser removida: ");
          String nomeEscolaRemover = input.nextLine().toUpperCase();

          NoArvore removerBST = bst.buscar(nomeEscolaRemover);

          if (removerBST == null) {
            System.out.println("Escola nao encontrada na arvore BST");
          } else {
            bst.remover(nomeEscolaRemover);
            System.out.println("Escola " + nomeEscolaRemover + " removida com sucesso na arvore BST");
          }

          NoArvore removerAVL = avl.buscar(nomeEscolaRemover);

          if (removerAVL == null) {
            System.out.println("Escola nao encontrada na arvore AVL");
          } else {
            avl.remover(nomeEscolaRemover);
            System.out.println("Escola " + nomeEscolaRemover + " removida com sucesso na arvore AVL");
          }
          break;

        case 4:
          input.nextLine();

          System.out.println("\nSelecione uma análise:");
          System.out.println("[1] - Número de escolas com alunos especiais por município");
          System.out.println("[2] - Número de alunos com deficiência por escola");
          System.out.println("[3] - Número de escolas com DA / DF / DM / DV");
          System.out.println("[4] - Escola com maior quantidade de alunos");
          System.out.println("[5] - Comparações realizadas nas árvores BST e AVL");
          System.out.println("[6] - Voltar ao menu principal");

          int opcaoAnalise = input.nextInt();

          switch (opcaoAnalise) {
            case 1: // Número de escolas com alunos especiais por município
              Map<String, Integer> escolasPorMunicipio = bst.contarEscolasPorMunicipio();
              for (Map.Entry<String, Integer> entry : escolasPorMunicipio.entrySet()) {
                System.out.println("Município: " + entry.getKey() + " - Escolas: " + entry.getValue());
              }
              break;

            case 2: // Número de alunos com deficiência por escola
              Map<String, Integer> alunosPorEscola = bst.contarAlunosPorEscola();
              for (Map.Entry<String, Integer> entry : alunosPorEscola.entrySet()) {
                System.out.println("Escola: " + entry.getKey() + " - Alunos: " + entry.getValue());
              }
              break;

            case 3: // Número de escolas com DA / DF / DM / DV
              bst.contarEscolasEspeciais();
              break;

            case 4: // Escola com maior quantidade de alunos
              NoArvore escolaComMaisAlunos = bst.buscarEscolaComMaisAlunos();
              if (escolaComMaisAlunos != null) {
                System.out.println("Escola com maior quantidade de alunos: " + escolaComMaisAlunos.escola
                    + " (" + escolaComMaisAlunos.alunos + " alunos)");
              } else {
                System.out.println("Nenhuma escola encontrada.");
              }
              break;

            case 5: // Comparações realizadas nas árvores BST e AVL
              System.out.println("Comparações na BST: " + bst.comparacoes);
              System.out.println("Comparações na AVL: " + avl.comparacoes);
              break;

            case 6: // Voltar ao menu principal
              System.out.println("Retornando ao menu principal...");
              break;

            default:
              System.out.println("Opção inválida.");
              break;
          }
          break;

        case 5:
          System.out.println("Saindo...");
          input.close();
          System.exit(0);
          break;
        default:
          System.out.println("Opcao invalida. Selecione um numero valido no menu");
          break;
      }
    }
  }
}
