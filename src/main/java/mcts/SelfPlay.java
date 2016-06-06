package mcts;

import java.util.Random;

public class SelfPlay<S extends State> {
    private final Random random = new Random();

    private final Mcts<S> mcts1;
    private final Mcts<S> mcts2;

    private S state;

    public SelfPlay(
        S state,
        int threads1,
        int threads2,
        int timePerActionSec1,
        int timePerActionSec2,
        int maxIterations1,
        int maxIterations2) {

        this.state = state;
        mcts1 = new Mcts<>(threads1, timePerActionSec1, maxIterations1);
        mcts2 = new Mcts<>(threads2, timePerActionSec2, maxIterations2);
    }

    @SuppressWarnings("unchecked")
    public int play() {
        int c = random.nextInt(2);
        int player = 0;
        int action = -1;
        while (!state.isTerminal()) {
            player = 1 + c++ % 2;
            Mcts<S> mcts = player == 1
                ? mcts1
                : mcts2;

            mcts.setRoot(action, state);
            mcts.think();
            state = (S) mcts.takeAction();
            action = mcts.getLastAction();
        }
        boolean draw = state.getWinner() == 0;
        return !draw
            ? player
            : 0;
    }

    public void stop() {
        mcts1.stop();
        mcts2.stop();
    }

}
