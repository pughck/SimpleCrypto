package edu.rosehulman.pughck.simplecrypto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.client.Firebase;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.adapters.ConversationAdapter;
import edu.rosehulman.pughck.simplecrypto.models.MessageModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 *
 */
public class ConversationFragment extends Fragment {

    private static final String ARG_CONVERSATION_KEY = "conversation_key";

    private String mConversationKey;

    public ConversationFragment() {

        // Required empty public constructor
    }

    public static ConversationFragment newInstance(String conversationKey) {

        ConversationFragment fragment = new ConversationFragment();

        Bundle args = new Bundle();
        args.putString(ARG_CONVERSATION_KEY, conversationKey);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mConversationKey = getArguments().getString(ARG_CONVERSATION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.conversation_recycler_view);
        final ConversationAdapter adapter = new ConversationAdapter(getContext(), mConversationKey);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(getContext()));
        rView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new SwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rView);

        final EditText messageText = (EditText) rootView.findViewById(R.id.message_edit_text);

        // TODO long press brings up saved strings with correct encryption if any

        View send = rootView.findViewById(R.id.send_message);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String messageVal = messageText.getText().toString();

                if (messageVal.isEmpty()) {
                    return;
                }

                adapter.sendMessage(messageVal);

                messageText.setText("");
            }
        });

        return rootView;
    }
}
