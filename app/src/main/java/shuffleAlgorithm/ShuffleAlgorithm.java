package shuffleAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShuffleAlgorithm {

    HashMap<String, Integer> allImages;
    HashMap<String, Integer> highRated;
    HashMap<String, Integer> lowRated;
    //List<Map.Entry<String, Integer>> is an instance such that every element is a pair. it has key and value: <image name, its rating>
    List<Map.Entry<String, Integer>> correctAnswer;

    double avg, dev;

    /**
     * constructor
     * @param allImages - the entir images collection. crucial for initializing everything.
     */
    public ShuffleAlgorithm(HashMap<String, Integer> allImages){
        this.allImages = allImages;
        avg = calculateAVG(allImages);
        dev = calcDeviation(allImages);
        highRated = chooseHighRated(allImages, avg, dev);
        lowRated = chooseLowRated(allImages, avg, dev);
    }

    public double getAvg() {
        return avg;
    }

    public double getDev() {
        return dev;
    }

    public HashMap<String, Integer> getAllImages() {
        return allImages;
    }

    public HashMap<String, Integer> getHighRated() {
        return highRated;
    }

    public HashMap<String, Integer> getLowRated() {
        return lowRated;
    }

    //List<Map.Entry<String, Integer>> is an instance such that every element is a pair. it has key and value: <image name, its rating>
    public List<Map.Entry<String, Integer>> getCorrectAnswer() {
        return correctAnswer;
    }

    private  double calculateAVG(HashMap<String, Integer> v){
        double sum = 0.00;
        for (String key: v.keySet()){
            sum+=v.get(key);
        }
        return sum/v.size();
    }

    private  double calcVariance(HashMap<String, Integer> v){
        double sqrDiff = 0.00;
        double avg = calculateAVG(v);
        for (String key: v.keySet()){
            sqrDiff+= (v.get(key)-avg)*(v.get(key)-avg);
        }
        return sqrDiff/v.size();
    }

    private  double calcDeviation(HashMap<String, Integer> v){
        return Math.sqrt(calcVariance(v));
    }

    private HashMap<String, Integer> chooseHighRated(HashMap<String, Integer> v, double avg, double dev){
        HashMap<String, Integer> highV = new HashMap<>();
        if (v.isEmpty())
            return highV;
        double highRating = avg+dev;
        for (String key: v.keySet()){
            if (v.get(key) > highRating) {
                highV.put(key, v.get(key));
            }
        }
        return highV;
    }


    private HashMap<String, Integer> chooseLowRated(HashMap<String, Integer> v, double avg, double dev){
        HashMap<String, Integer> lowV = new HashMap<>();
        if (v.isEmpty())
            return lowV;
        double highRating = avg+dev;
        for (String key: v.keySet()){
            if (v.get(key) <= highRating) {
                lowV.put(key, v.get(key));
            }
        }
        return lowV;
    }

    private List<Map.Entry<String, Integer>> getMapList(Map<String, Integer> m){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(m.entrySet());
        return list;
    }

    private HashMap<String, Integer> selectNRandElements(HashMap<String, Integer> v, int n){
        HashMap<String, Integer> mV = new HashMap<>();
        if (v.isEmpty())
            return mV;
        List<Map.Entry<String, Integer>> vlist = getMapList(v);
        int max = v.size();
        for (int i=0; i<n; i++){
            int rand = (int)(Math.random()*max);
            Map.Entry<String, Integer> e = vlist.get(rand);
            mV.put(e.getKey(), e.getValue());
            Map.Entry<String, Integer> last = vlist.get(max-1);
            vlist.remove(rand);
            vlist.add(rand, last);
            max--;
        }
        return mV;
    }

    /**
     * get a list as decribed in the field variables {@see correctAnswer}
     * @param high - number of high rated to be chosen
     * @param low - - number of low rated to be chosen
     * @return a map entry list: <image name, rating>, can extract data with entry accessors (getKey(), getValue())
     */
    public List<Map.Entry<String, Integer>> shuffle( int high, int low){
        List<Map.Entry<String, Integer>> shuffledImages = new ArrayList<>();
        HashMap<String, Integer> highChosen = selectNRandElements(highRated, high);
        correctAnswer = getMapList(highChosen);
        HashMap<String, Integer> lowChosen = selectNRandElements(lowRated, low);
        shuffledImages.addAll(getMapList(highChosen));
        shuffledImages.addAll(getMapList(lowChosen));
        Collections.shuffle(shuffledImages);
        return shuffledImages;
    }



}
