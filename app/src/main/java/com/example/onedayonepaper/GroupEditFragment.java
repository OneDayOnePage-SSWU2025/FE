package com.example.onedayonepaper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.onedayonepaper.data.api.AladinApiClient;
import com.example.onedayonepaper.data.api.AladinApiService;
import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.request.ChangeBookRequest;
import com.example.onedayonepaper.data.item.AladinBookItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class GroupEditFragment extends Fragment {

    private int groupId;

    EditText etName, etCode;
    Spinner spinnerTheme;
    TextView tvSpinnerPlaceholder;
    AutoCompleteTextView etSearchBook;
    TextView tvSelectedBook;
    AppCompatButton btnSave;

    private AladinBookItem selectedBook = null;

    private List<String> searchTitles = new ArrayList<>();
    private List<String> searchIsbn13 = new ArrayList<>();

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private boolean userSelectedItem = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.group_edit, container, false);

        etName = view.findViewById(R.id.etName);
        etCode = view.findViewById(R.id.etCode);
        spinnerTheme = view.findViewById(R.id.spinnerTheme);
        tvSpinnerPlaceholder = view.findViewById(R.id.tvSpinnerPlaceholder);
        etSearchBook = view.findViewById(R.id.etSearchBook);
        tvSelectedBook = view.findViewById(R.id.tvSelectedBook);
        btnSave = view.findViewById(R.id.btnCreate);

        ImageButton back = view.findViewById(R.id.backButton);
        back.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        readBundle();
        lockUI();
        setupBookSearch(view);

        btnSave.setOnClickListener(v -> updateBook());

        return view;
    }

    private void readBundle() {
        Bundle b = getArguments();
        if (b != null) {
            groupId = b.getInt("groupId");
            etName.setText(b.getString("groupName"));
            etCode.setText(String.valueOf(b.getInt("code")));
            tvSpinnerPlaceholder.setText(b.getString("theme"));
            tvSelectedBook.setText(b.getString("bookName"));
        }
    }

    private void lockUI() {
        etName.setEnabled(false);
        etCode.setEnabled(false);
        spinnerTheme.setEnabled(false);
        tvSpinnerPlaceholder.setEnabled(false);
    }

    private void setupBookSearch(View rootView) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(requireContext(), R.layout.item_spinner_dropdown, searchTitles);

        etSearchBook.setAdapter(adapter);
        etSearchBook.setThreshold(1);

        etSearchBook.setOnClickListener(v -> etSearchBook.showDropDown());
        tvSelectedBook.setOnClickListener(v -> {
            etSearchBook.requestFocus();
            etSearchBook.showDropDown();
        });

        etSearchBook.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                userSelectedItem = false;

                if (searchRunnable != null)
                    searchHandler.removeCallbacks(searchRunnable);

                String q = s.toString().trim();
                if (q.length() < 2) return;

                searchRunnable = () -> searchBooks(q);
                searchHandler.postDelayed(searchRunnable, 120);
            }
        });

        etSearchBook.setOnItemClickListener((parent, view, pos, id) -> {
            if (pos >= searchIsbn13.size()) return;

            String title = searchTitles.get(pos);
            String isbn = searchIsbn13.get(pos);

            etSearchBook.setText(title);
            etSearchBook.setSelection(title.length());
            tvSelectedBook.setText(title);

            searchBookInfo(isbn);
        });

        etSearchBook.post(() -> {
            View container = rootView.findViewById(R.id.bookSearchContainer);
            etSearchBook.setDropDownAnchor(container.getId());
            etSearchBook.setDropDownWidth(container.getWidth());
        });
    }

    private void searchBooks(String query) {

        String KEY = "//TODO";
        AladinApiService api = AladinApiClient.getClient().create(AladinApiService.class);

        api.searchBooks(KEY, query, "Title", "Book", 1, 10, "xml", "20131101")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                        try {
                            String xml = res.body().string();
                            List<AladinBookItem> books = parseSearch(xml);

                            searchTitles.clear();
                            searchIsbn13.clear();

                            for (AladinBookItem b : books) {
                                searchTitles.add(b.getTitle());
                                searchIsbn13.add(b.getIsbn13());
                            }

                            ArrayAdapter<String> newAdapter =
                                    new ArrayAdapter<>(requireContext(),
                                            R.layout.item_spinner_dropdown,
                                            new ArrayList<>(searchTitles));

                            etSearchBook.setAdapter(newAdapter);
                            etSearchBook.showDropDown();

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    @Override public void onFailure(Call<ResponseBody> call, Throwable t) {}
                });
    }

    private List<AladinBookItem> parseSearch(String xml) throws Exception {

        List<AladinBookItem> list = new ArrayList<>();

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(xml));

        AladinBookItem cur = null;
        boolean inItem = false;
        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();

            if (event == XmlPullParser.START_TAG) {
                if ("item".equals(tag)) {
                    cur = new AladinBookItem();
                    inItem = true;
                } else if (inItem) {
                    if ("title".equals(tag)) cur.setTitle(parser.nextText());
                    if ("isbn13".equals(tag)) cur.setIsbn13(parser.nextText());
                }
            }

            if (event == XmlPullParser.END_TAG && "item".equals(tag)) {
                list.add(cur);
            }

            event = parser.next();
        }

        return list;
    }

    private void searchBookInfo(String isbn13) {

        String KEY = "//TODO";
        AladinApiService api = AladinApiClient.getClient().create(AladinApiService.class);

        api.lookupBook(KEY, "ISBN13", isbn13, "xml", "20131101", "authors,fulldescription")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                        try {
                            String xml = res.body().string();
                            selectedBook = parseDetail(xml);

                            if (selectedBook != null)
                                Toast.makeText(requireContext(), "도서정보 로드 완료!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    @Override public void onFailure(Call<ResponseBody> call, Throwable t) {}
                });
    }

    private AladinBookItem parseDetail(String xml) throws Exception {

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(xml));

        AladinBookItem item = null;
        boolean inItem = false;
        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            String tag = parser.getName();

            if (event == XmlPullParser.START_TAG) {
                if ("item".equals(tag)) {
                    item = new AladinBookItem();
                    inItem = true;
                }
                else if (inItem) {
                    if ("title".equals(tag)) item.setTitle(parser.nextText());
                    if ("author".equals(tag)) item.setAuthor(parser.nextText());
                    if ("cover".equals(tag)) item.setImgUrl(parser.nextText());
                    if ("itemPage".equals(tag)) {
                        try { item.setTotalPage(Integer.parseInt(parser.nextText())); }
                        catch (Exception e) { item.setTotalPage(0); }
                    }
                }
            }

            event = parser.next();
        }

        return item;
    }

    private void updateBook() {

        if (selectedBook == null) {
            Toast.makeText(requireContext(), "수정할 책을 선택해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangeBookRequest req = new ChangeBookRequest(
                selectedBook.getImgUrl(),
                selectedBook.getTitle(),
                selectedBook.getAuthor(),
                selectedBook.getTotalPage()
        );

        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.changeBook(groupId, req)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> res) {
                        if (res.isSuccessful()) {
                            Toast.makeText(requireContext(), "도서 변경 완료!", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    }

                    @Override public void onFailure(Call<Void> call, Throwable t) {}
                });
    }
}
