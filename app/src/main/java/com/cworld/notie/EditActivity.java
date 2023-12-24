package com.cworld.notie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cworld.notie.adapter.NoteModel;
import com.cworld.notie.util.NoteHelper;
import com.cworld.notie.util.PreferenceHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class EditActivity extends AppCompatActivity {
    String originTitle;

    // components
    MaterialToolbar topAppBar;
    BottomAppBar bottomAppBar;
    EditText editHeader;
    EditText editBody;
    TextView markdownView;
    MenuItem menuView;
    MenuItem menuDone;
    MenuItem menuShare;

    // utils
    Markwon markwon;
    PreferenceHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        // components
        topAppBar = findViewById(R.id.topAppBar);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        editHeader = findViewById(R.id.editHeader);
        editBody = findViewById(R.id.editBody);
        markdownView = findViewById(R.id.markdownView);
        // menu
        Menu menu = topAppBar.getMenu();
        menuView = menu.findItem(R.id.item_preview);
        menuDone = menu.findItem(R.id.item_done);
        menuShare = menu.findItem(R.id.item_share);
        // obtain an instance of markdown component
        markwon = Markwon.create(this);
        // preference
        prefHelper = new PreferenceHelper(this);

        initTopAppBar();
        initBottomAppBar();
        originTitle = getIntent().getStringExtra("title");
        initHeader(originTitle);
        initBody(getIntent().getStringExtra("content"));
        initPreview();
    }

    private void setViewStat(boolean isView) {
        if (!isView) {
            editBody.setVisibility(View.VISIBLE);
            markdownView.setVisibility(View.GONE);
            return;
        }
        // set markdown
        markwon.setMarkdown(markdownView, editBody.getText().toString());
        editBody.setVisibility(View.GONE);
        markdownView.setVisibility(View.VISIBLE);
    }

    private void setEditStat(boolean isEdit) {
        if (isEdit) {
            // update stat & view
            setViewStat(false);
            menuDone.setVisible(true);
            menuShare.setVisible(false);
            menuView.setVisible(false);
            bottomAppBar.setVisibility(View.VISIBLE);

            if (editHeader.hasFocus() || editBody.hasFocus()) {
                return;
            }
            editBody.requestFocus();
            editBody.setSelection(editBody.getText().length());

            InputMethodManager imm = (InputMethodManager)
                    editBody.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editBody, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        // return normal style
        menuDone.setVisible(false);
        menuShare.setVisible(true);
        menuView.setVisible(true);
        bottomAppBar.setVisibility(View.GONE);

        // save file
        String title = editHeader.getText().toString();
        if (title.equals("")) {
            title = getString(R.string.edit_bar_name);
            editHeader.setText(title);
        }

        NoteModel note = new NoteModel(
                title,
                editBody.getText().toString(),
                Calendar.getInstance().getTime()
        );

        // check if replace file is needed
        if (originTitle == null || Objects.equals(originTitle, title))
            NoteHelper.setNote(note);
        else {
            NoteHelper.setNote(note, originTitle);
            originTitle = title;
        }
    }

    private void initTopAppBar() {
        topAppBar.setNavigationOnClickListener(v -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_done) {
                editHeader.clearFocus();
                editBody.clearFocus();
                setEditStat(false);
            } else if (itemId == R.id.item_preview) {
                setViewStat(true);
            } else if (itemId == R.id.item_share) {
                share();
            } else if (itemId == R.id.item_info) {
                showInfo();
            } else if (itemId == R.id.item_delete) {
                NoteHelper.deleteNote(originTitle);
                finish();
            } else return false;
            return true;
        });
    }

    private void initHeader(String title) {
        // set title content
        editHeader.setText(title);
        if (title != null) {
            topAppBar.setTitle(title);
        }

        // set header focus with action
        editHeader.setOnFocusChangeListener((v, hasFocus) -> setEditStat(hasFocus));

        // watch text change (sync with top appbar)
        editHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title;
                if (s.length() == 0)
                    title = getString(R.string.edit_bar_name);
                else
                    title = s.toString();
                topAppBar.setTitle(title);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initBody(String content) {
        // set lineHeight
        editBody.setLineSpacing(prefHelper.getLineHeight(), 1);
        // set body content
        editBody.setText(content);

        // set body focus with action
        editBody.setOnFocusChangeListener((v, hasFocus) -> setEditStat(hasFocus));

        // set content markdown highlight
        if (prefHelper.getHighlightEnabled()) return;

        // firstly render markdown content
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        editor.preRender(editBody.getText(), (MarkwonEditor.PreRenderResultListener) result -> {
            // it's wise to check if rendered result is for the same input,
            // for example by matching raw input
            if (editBody.getText().toString().equals(result.resultEditable().toString())) {
                // if you are in background thread do not forget
                // to execute dispatch in main thread
                result.dispatchTo(editBody.getText());
            }
        });

        // set edit render markdown listener (with thread)
        editBody.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
        editBody.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                editBody));
    }

    private void initBottomAppBar() {
        // default to hide bottom bar
        bottomAppBar.setVisibility(View.GONE);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            final int itemId = item.getItemId();
            int start = Math.max(editBody.getSelectionStart(), 0);
            int end = Math.max(editBody.getSelectionEnd(), 0);
            Editable editable = editBody.getText();
            if (itemId == R.id.actionCommand) {

            } else if (itemId == R.id.textBold) {
                insertPhrase(editable, "**", "**", start, end);
            } else if (itemId == R.id.textItalic) {
                insertPhrase(editable, "*", "*", start, end);
            } else if (itemId == R.id.textStrikethrough) {
                insertPhrase(editable, "~~", "~~", start, end);
            } else if (itemId == R.id.textQuote) {
                insertPhrase(editable, "> ", start);
            } else if (itemId == R.id.textList) {
                insertPhrase(editable, "- ", start);
            } else if (itemId == R.id.textNumList) {
                insertPhrase(editable, "1. ", start);
            } else if (itemId == R.id.textTag) {
                insertPhrase(editable, "# ", start);
            } else return false;
            return true;
        });
    }

    private void initPreview() {
        if (prefHelper.getPreviewEnabled()) setViewStat(true);
        // create editor
        markdownView.setOnClickListener(v -> setEditStat(true));
    }

    private void insertPhrase(@NonNull Editable editable, String phrase, int start) {
        // quick insert chips of text
        int length = editable.length(), lineStart = 0;
        if (start >= length) lineStart = length;
        else if (start > 0) { // search for newlines until find line start
            for (int i = start - 1; i >= 0; i--) {
                if (editable.charAt(i) == '\n') lineStart = i + 1;
            }
        }
        editable.insert(lineStart, phrase);
    }

    private void insertPhrase(Editable editable,
                              String phrase1, String phrase2, int start, int end) {
        // If selected, auto add to before & after of select
        if (start != end) {
            editable.replace(start, end,
                    new SpannableStringBuilder(
                            phrase1 + editable.subSequence(start, end) + phrase2),
                    0, phrase1.length() + phrase2.length() + end - start);
        } else {
            editable.insert(start, phrase1 + phrase2);
        }
    }

    private void share() {
        String content = editHeader.getText().toString();
        if (!content.equals("")) content += "\n";
        content += editBody.getText().toString();

        if (content.equals("")) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.no_text_alert,
                    Snackbar.LENGTH_SHORT
            ).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        // set share info title
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title));
        // open share activity
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to_title)));
    }

    void showInfo() {
        String text = editBody.getText().toString();
        // calc words
        String[] words = text.trim().split("\\s+");
        // calc lines
        String[] lines = text.split("\r\n|\r|\n");
        int lineCount = lines.length;

        String textFormat = getString(R.string.dialog_content);
        String statisticsText = String.format(textFormat, text.length(), words.length, lines.length);
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(statisticsText)
                .setPositiveButton(R.string.dialog_accept, null)
                .show();
    }
}