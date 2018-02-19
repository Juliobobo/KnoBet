package fr.kb.knobet.Model;

/**
 * Created by Julien on 19/02/2018.
 */

public class QuestionScore {
    private String QuestionScore;
    private String User;
    private String Score;

    public QuestionScore() {
    }

    public QuestionScore(String questionScore, String user, String score) {
        questionScore = questionScore;
        User = user;
        Score = score;
    }

    public String getQuestionScore() {
        return QuestionScore;
    }

    public void setQuestionSore(String questionSore) {
        QuestionScore = questionSore;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
