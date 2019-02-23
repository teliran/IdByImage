package com.hey.idbyimage.idbyimage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class ShuffelAlgorithmTest {

    private ShuffleAlgorithm shuffleAlgo;
    HashMap<String, Integer> allImages;
    double avg, dev;

    @Before
    public void setUp() throws Exception {
        allImages = new HashMap<>();
        for (int i=1; i<51; i++){
            allImages.put("p"+i,(int)(Math.random()*10)+1);
        }
        shuffleAlgo = new ShuffleAlgorithm(allImages);
        avg = shuffleAlgo.getAvg();
        dev = shuffleAlgo.getDev();

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void getAllImages() {
        assertTrue("The length of the maps should be the same",allImages.size() == shuffleAlgo.getAllImages().size());
        assertTrue("The maps should contain the same images",allImages.get("p32")==shuffleAlgo.getAllImages().get("p32"));
    }

    @Test
    public void getHighRated() {
        int imageCounter = 0;
        String image = "";
        for (String pic : allImages.keySet()){
            if (allImages.get(pic) > avg+dev){
                imageCounter++;
                if (image.length()==0){
                    image = pic;
                }
            }
        }
        assertTrue(shuffleAlgo.getHighRated().size()==imageCounter);
        assertTrue(allImages.get(image)==shuffleAlgo.getHighRated().get(image));
    }

    @Test
    public void getLowRated() {
        int imageCounter = 0;
        String image = "";
        for (String pic : allImages.keySet()){
            if (allImages.get(pic) <= avg+dev){
                imageCounter++;
                if (image.length()==0){
                    image = pic;
                }
            }
        }
        assertTrue(shuffleAlgo.getLowRated().size()==imageCounter);
        assertTrue(allImages.get(image)==shuffleAlgo.getLowRated().get(image));
    }

    @Test
    public void getCorrectAnswer() {
        shuffleAlgo.shuffle(3, 6);
        List<Map.Entry<String, Integer>> correctAns = shuffleAlgo.getCorrectAnswer();
        assertTrue(correctAns.size()==3);
        for (Map.Entry<String, Integer> entry: correctAns) {
            assertTrue(allImages.get(entry.getKey()) == entry.getValue());
            assertTrue(shuffleAlgo.getHighRated().get(entry.getKey()) == entry.getValue());

        }
    }

    @Test
    public void shuffle() {
        List<Map.Entry<String, Integer>> shuffle = shuffleAlgo.shuffle(3, 6);

        assertTrue(shuffle.size() == 9);
        for (Map.Entry<String, Integer> entry: shuffle){
            assertTrue(entry.getValue() == allImages.get(entry.getKey()));
            if (entry.getValue()> dev+avg){
                assertTrue(entry.getValue() == shuffleAlgo.getHighRated().get(entry.getKey()));
            }
            else{
                assertTrue(entry.getValue() == shuffleAlgo.getLowRated().get(entry.getKey()));
            }
        }
    }

    private void setExtremeRatings(HashMap<String, Integer> m){
        for (int i=0; i<m.size(); i++){
            m.put("p"+i, 1);
        }
    }
}
