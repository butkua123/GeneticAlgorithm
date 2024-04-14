import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    
    // Kromozom sınıfı tanımı.
    static class Chromosome {
        String genes;
        int fitness;
        
        public Chromosome(String genes) {
            this.genes = genes;
            this.fitness = 0;
        }
        
        // Uygunluk fonksiyonu: Hedef şifreye ne kadar yakın olduğunu hesaplar.
        public void calculateFitness(String target) {
            this.fitness = 0;
            for (int i = 0; i < this.genes.length(); i++) {
                if (this.genes.charAt(i) != target.charAt(i)) {
                    this.fitness++;
                }
            }
        }
    }
    
    // Rastgele kromozom oluşturma.
    public static String createRandomGenes(int length) {
        Random rnd = new Random();
        StringBuilder genes = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (' ' + rnd.nextInt(95)); // ASCII aralığında rastgele karakterler
            genes.append(c);
        }
        return genes.toString();
    }
    
    // Başlangıç popülasyonunu oluşturma.
    public static List<Chromosome> createInitialPopulation(int size, int chromosomeLength, String target) {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String genes = createRandomGenes(chromosomeLength);
            Chromosome chromosome = new Chromosome(genes);
            chromosome.calculateFitness(target);
            population.add(chromosome);
        }
        return population;
    }
    
    // Seçilim: En iyi uygunluğa sahip kromozomları seçer.
    public static List<Chromosome> selectTheFittest(List<Chromosome> population, int numToSelect) {
        population.sort(Comparator.comparingInt(c -> c.fitness));
        return population.subList(0, numToSelect);
    }
    
    // Çaprazlama (Crossover): İki kromozomun genlerini birleştirerek yeni bir kromozom oluşturur.
    public static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        Random rnd = new Random();
        int splitPoint = rnd.nextInt(parent1.genes.length());
        String childGenes = parent1.genes.substring(0, splitPoint) + parent2.genes.substring(splitPoint);
        return new Chromosome(childGenes);
    }
    
    // Mutasyon: Bir kromozomdaki rastgele bir geni değiştirir.
    public static void mutate(Chromosome chromosome, double mutationRate) {
        Random rnd = new Random();
        StringBuilder genes = new StringBuilder(chromosome.genes);
        for (int i = 0; i < genes.length(); i++) {
            if (rnd.nextDouble() < mutationRate) {
                char newGene = (char) (' ' + rnd.nextInt(95));
                genes.setCharAt(i, newGene);
            }
        }
        chromosome.genes = genes.toString();
    }
    
    public static void main(String[] args) {
      String target = "Generative AI";
      int populationSize = 100;
      int chromosomeLength = target.length();
      double mutationRate = 0.01;
      int maxGenerations = 1000;
      int fittestToSelect = 50;
  
      // Random nesnesi tanımlama
      Random rnd = new Random();
  
      // Başlangıç popülasyonunu oluşturma.
      List<Chromosome> population = createInitialPopulation(populationSize, chromosomeLength, target);
  
      Chromosome best = null;
      long startTime = System.currentTimeMillis();
      int generation = 0; // Generation değişkenini döngü dışında tanımla.
  
      // Ana algoritma döngüsü.
      for (; generation < maxGenerations; generation++) {
          // En uygun bireyleri seçme.
          List<Chromosome> fittest = selectTheFittest(population, fittestToSelect);
          best = fittest.get(0); // En uygun bireyi sakla.
  
          // Hedefe ulaşıp ulaşmadığımızı kontrol et.
          if (best.fitness == 0) {
              break; // Şifreyi bulduk.
          }
  
          // Yeni popülasyon oluşturma.
          List<Chromosome> newPopulation = new ArrayList<>(fittest);
          while (newPopulation.size() < populationSize) {
              // Çaprazlama
              Chromosome parent1 = fittest.get(rnd.nextInt(fittest.size()));
              Chromosome parent2 = fittest.get(rnd.nextInt(fittest.size()));
              Chromosome child = crossover(parent1, parent2);
  
              // Mutasyon
              mutate(child, mutationRate);
  
              // Yeni bireyi popülasyona ekle.
              child.calculateFitness(target);
              newPopulation.add(child);
          }
  
          population = newPopulation;
  
          // İstatistikleri yazdırma.
          System.out.printf("Generation: %d, Best fitness: %d, Genes: %s\n", generation, best.fitness, best.genes);
      }
  
      long endTime = System.currentTimeMillis();
      System.out.printf("Found solution in %d generations and %d ms\n", generation, endTime - startTime);
      System.out.println("Solution: " + best.genes);
  }  
}
