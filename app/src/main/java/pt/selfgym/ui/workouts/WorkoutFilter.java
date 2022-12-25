package pt.selfgym.ui.workouts;

import java.util.Objects;

public class WorkoutFilter {
    private boolean upperBody;
    private boolean lowerBody;
    private boolean fullBody;
    private boolean push;
    private boolean pull;

    public WorkoutFilter() {
        this.upperBody = false;
        this.fullBody = false;
        this.lowerBody = false;
        this.push = false;
        this.pull = false;
    }

    public boolean isUpperBody() {
        return upperBody;
    }

    public void setUpperBody(boolean upperBody) {
        this.upperBody = upperBody;
    }

    public boolean isLowerBody() {
        return lowerBody;
    }

    public void setLowerBody(boolean lowerBody) {
        this.lowerBody = lowerBody;
    }

    public boolean isFullBody() {
        return fullBody;
    }

    public void setFullBody(boolean fullBody) {
        this.fullBody = fullBody;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }

    public boolean filter(String workoutType){
        if (!upperBody && !lowerBody && !fullBody && !push && !pull){
            return true;
        } else if (workoutType.equals("upper body")){
            if (upperBody){
                return true;
            }
        } else if (workoutType.equals("lower body")){
            if (lowerBody){
                return true;
            }
        } else if (workoutType.equals("full body")){
            if (fullBody){
                return true;
            }
        } else if (workoutType.equals("push")) {
            if (push) {
                return true;
            }
        } else if (workoutType.equals("pull")){
            if (pull){
                return true;
            }
        }
        return false;
    }
}
