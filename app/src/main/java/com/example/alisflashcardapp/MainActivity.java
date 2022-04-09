package com.example.alisflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView questionTextView;
    TextView answerTextView;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.flashcard_question);
        answerTextView = findViewById(R.id.flashcard_answer);

        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextView.setVisibility(View.INVISIBLE);
                answerTextView.setVisibility(View.VISIBLE);
            }





        });

        ImageView addQuestionImageView = findViewById(R.id.flashcard_add_button);
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_add_card.class);
          //  startActivity(intent);
            startActivityForResult(intent, 100);
            }


        });

        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size() > 0) {
            Flashcard firstcard = allFlashcards.get(0);
            questionTextView.setText(firstcard.getQuestion());
            answerTextView.setText(firstcard.getAnswer());
        }

        findViewById(R.id.flashcard_next_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardIndex *= 1;

                if (cardIndex < allFlashcards.size()) {
                    Snackbar.make(view,
                           "You've reacher the end of the cards! Going back to the start.",
                           Snackbar.LENGTH_SHORT)
                    .show();

                    cardIndex = 0; //resetting index
                }



                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing

                        questionTextView.startAnimation(rightInAnim);


                        Flashcard currentcard = allFlashcards.get(cardIndex);
                        questionTextView.setText(currentcard.getQuestion());
                        answerTextView.setText(currentcard.getAnswer());

                        questionTextView.setVisibility(View.VISIBLE);
                        answerTextView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                questionTextView.startAnimation(leftOutAnim);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {

            if (data != null) {
               String questionString =  data.getExtras().getString("QUESTION_KEY");
               String answerString = data.getExtras().getString("ANSWER_KEY");
                questionTextView.setText(questionString);
                answerTextView.setText(answerString);

               Flashcard flashcard = new Flashcard(questionString, answerString);
               flashcardDatabase.insertCard(flashcard);


               allFlashcards = flashcardDatabase.getAllCards();

            }
        }
    }
}



