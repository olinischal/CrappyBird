package birdmandemo;


class User{
    int score = 0;
    String userName;

    public User(String userName){
        this.userName = userName;
    }

    public void setScore(int userScore){
        if (userScore > score){
            score = userScore;
        }else{
            this.score = score;
        }
    }
    public void setUserName(String user){
        userName = user;
    }

    public int getScore(){
        return score;
    }
    public String getName(){
        return userName;
    }

    public void print(){
        System.out.println("The value of " + userName + " is: " + score);
    }
}