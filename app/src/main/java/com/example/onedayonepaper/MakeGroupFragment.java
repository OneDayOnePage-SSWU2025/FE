package com.example.onedayonepaper;

import android.graphics.Color;
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
import android.widget.LinearLayout;
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
import com.example.onedayonepaper.data.dto.request.CreateGroupRequest;
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

public class MakeGroupFragment extends Fragment {

    Spinner spinnerTheme;
    TextView tvSpinnerPlaceholder;
    AutoCompleteTextView etSearchBook;
    TextView tvSelectedBook;
    EditText etName, etCode;
    AppCompatButton btnCreate;

    private AladinBookItem selectedBook = null;

    private final List<String> searchTitles = new ArrayList<>();
    private final List<String> searchIsbn13 = new ArrayList<>();
    private ArrayAdapter<String> searchAdapter;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private String selectedTheme = null;

    private boolean userSelectedItem = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.group_makegroup, container, false);

        spinnerTheme = view.findViewById(R.id.spinnerTheme);
        tvSpinnerPlaceholder = view.findViewById(R.id.tvSpinnerPlaceholder);
        etSearchBook = view.findViewById(R.id.etSearchBook);
        tvSelectedBook = view.findViewById(R.id.tvSelectedBook);
        etName = view.findViewById(R.id.etName);
        etCode = view.findViewById(R.id.etCode);
        btnCreate = view.findViewById(R.id.btnCreate);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack()
        );

        setupThemeSpinner();
        setupBookSearch(view);

        btnCreate.setOnClickListener(v -> createGroup());

        return view;
    }

    private void setupThemeSpinner() {

        String[] themeList = {
                "에세이/자기계발", "과학/IT", "시사", "사회",
                "고전문학", "추리소설", "판타지소설", "액션/모험",
                "로맨스/멜로", "청소년 문학", "아동/동화",
                "역사서", "시", "기타"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_closed_item,
                themeList
        );

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerTheme.setAdapter(adapter);
        spinnerTheme.setPopupBackgroundResource(R.drawable.spinner_popup_bg);

        spinnerTheme.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            boolean isFirstSelection = true;

            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent,
                                       View view, int position, long id) {

                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                selectedTheme = parent.getItemAtPosition(position).toString();
                tvSpinnerPlaceholder.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                tvSpinnerPlaceholder.setVisibility(View.VISIBLE);
                selectedTheme = null;
            }
        });

        tvSpinnerPlaceholder.setOnClickListener(v -> spinnerTheme.performClick());
    }

    private void setupBookSearch(View rootView) {

        searchAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_dropdown,
                searchTitles
        );

        etSearchBook.setAdapter(searchAdapter);
        etSearchBook.setDropDownBackgroundResource(R.drawable.spinner_popup_bg);
        etSearchBook.setThreshold(1);

        etSearchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                userSelectedItem = false;

                if (searchRunnable != null)
                    searchHandler.removeCallbacks(searchRunnable);

                String query = s.toString().trim();
                if (query.length() < 2) return;

                searchRunnable = () -> searchBooksFromAladin(query);
                searchHandler.postDelayed(searchRunnable, 100);
            }
        });

        etSearchBook.setOnItemClickListener((parent, view, position, id) -> {

            if (position >= searchIsbn13.size()) return;

            userSelectedItem = true;
            etSearchBook.dismissDropDown();

            String title = searchTitles.get(position);
            String isbn = searchIsbn13.get(position);

            tvSelectedBook.setBackgroundResource(R.drawable.spinner_item_selected);
            tvSelectedBook.setText(title);
            tvSelectedBook.setTextColor(Color.BLACK);

            searchBookInfoFromAladin(isbn);
        });

        etSearchBook.post(() -> {
            LinearLayout parentContainer = rootView.findViewById(R.id.bookSearchContainer);
            etSearchBook.setDropDownAnchor(R.id.bookSearchContainer);
            etSearchBook.setDropDownWidth(parentContainer.getWidth());
        });
    }

    private void searchBooksFromAladin(String query) {

        String TTB_KEY = "//TODO";

        AladinApiService api = AladinApiClient.getClient().create(AladinApiService.class);

        Call<ResponseBody> call = api.searchBooks(
                TTB_KEY, query, "Title", "Book",
                1, 10, "xml", "20131101"
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                try {
                    String xml = response.body().string();
                    List<AladinBookItem> result = parseAladinSearchResult(xml);

                    searchTitles.clear();
                    searchIsbn13.clear();

                    for (AladinBookItem b : result) {
                        searchTitles.add(b.getTitle());
                        searchIsbn13.add(b.getIsbn13());
                    }

                    searchAdapter = new ArrayAdapter<>(
                            requireContext(),
                            R.layout.item_spinner_dropdown,
                            new ArrayList<>(searchTitles)
                    );
                    etSearchBook.setAdapter(searchAdapter);


                    if (!userSelectedItem) {
                        etSearchBook.showDropDown();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchBookInfoFromAladin(String isbn13) {

        String TTB_KEY = "//TODO";

        AladinApiService api = AladinApiClient.getClient().create(AladinApiService.class);

        Call<ResponseBody> call = api.lookupBook(
                TTB_KEY,
                "ISBN13",
                isbn13,
                "xml",
                "20131101",
                "authors,fulldescription"
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), "알라딘 조회 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String xml = response.body().string();
                    selectedBook = parseAladinLookupResult(xml);

                    if (selectedBook != null) {
                        Toast.makeText(requireContext(), "도서정보 불러오기 완료", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(requireContext(), "API 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<AladinBookItem> parseAladinSearchResult(String xml) throws Exception {

        List<AladinBookItem> list = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));

        int eventType = parser.getEventType();
        AladinBookItem current = null;
        boolean inItem = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String tagName = parser.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:

                    if ("item".equalsIgnoreCase(tagName)) {
                        inItem = true;
                        current = new AladinBookItem();
                    } else if (inItem && current != null) {

                        if ("title".equalsIgnoreCase(tagName)) {
                            current.setTitle(parser.nextText());
                        } else if ("isbn13".equalsIgnoreCase(tagName)) {
                            current.setIsbn13(parser.nextText());
                        } else if ("author".equalsIgnoreCase(tagName)) {
                            String author = parser.nextText();
                            author = author.replaceAll("\\(.*?\\)", "").trim();
                            current.setAuthor(author);
                        } else if ("cover".equalsIgnoreCase(tagName)) {
                            current.setImgUrl(parser.nextText());
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    if ("item".equalsIgnoreCase(tagName) && inItem && current != null) {
                        inItem = false;
                        list.add(current);
                        current = null;
                    }
                    break;
            }

            eventType = parser.next();
        }

        return list;
    }

    private AladinBookItem parseAladinLookupResult(String xml) throws Exception {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));

        AladinBookItem item = null;
        boolean inItem = false;

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String tag = parser.getName();

            switch (eventType) {

                case XmlPullParser.START_TAG:

                    if ("item".equalsIgnoreCase(tag)) {
                        inItem = true;
                        item = new AladinBookItem();
                    }
                    else if (inItem && item != null) {

                        if ("title".equalsIgnoreCase(tag)) {
                            item.setTitle(parser.nextText());
                        }
                        else if ("author".equalsIgnoreCase(tag)) {
                            String author = parser.nextText();
                            author = author.replaceAll("\\(.*?\\)", "").trim();
                            item.setAuthor(author);
                        }
                        else if ("cover".equalsIgnoreCase(tag)) {
                            item.setImgUrl(parser.nextText());
                        }
                        else if ("itemPage".equalsIgnoreCase(tag)) {
                            try {
                                item.setTotalPage(Integer.parseInt(parser.nextText()));
                            } catch (Exception e) {
                                item.setTotalPage(0);
                            }
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    if ("item".equalsIgnoreCase(tag) && inItem) {
                        return item;
                    }
                    break;
            }

            eventType = parser.next();
        }

        return null;
    }

private void createGroup() {

    String groupName = etName.getText().toString().trim();
    String codeStr = etCode.getText().toString().trim();

    if (groupName.isEmpty()) {
        Toast.makeText(requireContext(), "교환독서방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    if (codeStr.isEmpty()) {
        Toast.makeText(requireContext(), "참가 코드를 입력해주세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    if (selectedTheme == null) {
        Toast.makeText(requireContext(), "그룹 테마를 선택해주세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    if (selectedBook == null) {
        Toast.makeText(requireContext(), "첫 교환도서를 선택해주세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    int code;
    try {
        code = Integer.parseInt(codeStr);
    } catch (Exception e) {
        Toast.makeText(requireContext(), "참가 코드는 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
        return;
    }

    CreateGroupRequest request = new CreateGroupRequest(
            groupName,
            code,
            selectedTheme,
            selectedBook.getImgUrl(),
            selectedBook.getTitle(),
            selectedBook.getAuthor(),
            selectedBook.getTotalPage()
    );

    ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

    api.createGroup(request).enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call,
                               Response<Void> response) {

            if (response.isSuccessful()) {
                Toast.makeText(requireContext(), "그룹 생성 완료!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(),
                        "생성 실패 (코드 " + response.code() + ")",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    });
}
}