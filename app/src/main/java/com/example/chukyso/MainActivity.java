package com.example.chukyso;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtP;
    EditText edtQ;
    EditText edtPrivateKey;
    EditText edtPublicKey;
    EditText edtMessage;
    EditText edtSignatureInput;

    TextView txtAscii;
    TextView txtSignature;
    TextView txtVerifyResult;

    Button btnRandom;
    Button btnGenerate;
    Button btnSign;
    Button btnVerify;

    RSA rsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        edtP = findViewById(R.id.edtP);
        edtQ = findViewById(R.id.edtQ);
        edtPrivateKey = findViewById(R.id.edtPrivateKey);
        edtPublicKey = findViewById(R.id.edtPublicKey);
        edtMessage = findViewById(R.id.edtMessage);
        edtSignatureInput = findViewById(R.id.edtSignatureInput);

        txtAscii = findViewById(R.id.txtAscii);
        txtSignature = findViewById(R.id.txtSignature);
        txtVerifyResult = findViewById(R.id.txtVerifyResult);

        btnRandom = findViewById(R.id.btnRandom);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSign = findViewById(R.id.btnSign);
        btnVerify = findViewById(R.id.btnVerify);

        rsa = new RSA();

        edtP.setText("13");
        edtQ.setText("17");

        btnRandom.setOnClickListener(v -> randomPrime());

        btnGenerate.setOnClickListener(v -> generateKeys());

        btnSign.setOnClickListener(v -> signMessage());

        btnVerify.setOnClickListener(v -> verifySignature());
    }

    private void generateKeys() {
        try {
            long p = Long.parseLong(edtP.getText().toString());
            long q = Long.parseLong(edtQ.getText().toString());

            rsa.generateKeys(p, q);

            edtPrivateKey.setText(
                    String.valueOf(rsa.getPrivateKey())
            );

            edtPublicKey.setText(
                    String.valueOf(rsa.getPublicKey())
            );

            Toast.makeText(
                    this,
                    "Sinh khóa thành công",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "P và Q không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void signMessage() {

        if (rsa.getN() == 0) {
            Toast.makeText(
                    this,
                    "Hãy sinh khóa trước",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String message = edtMessage.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(
                    this,
                    "Hãy nhập văn bản",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        byte[] bytes = message.getBytes();

        List<Long> ascii = new ArrayList<>();
        List<Long> signatures = new ArrayList<>();

        for (byte b : bytes) {
            int value = b & 0xFF;

            ascii.add((long) value);

            long signature = rsa.sign(value);

            signatures.add(signature);
        }

        txtAscii.setText(
                convertListToString(ascii)
        );

        txtSignature.setText(
                convertListToString(signatures)
        );

        edtSignatureInput.setText(
                convertListToString(signatures)
        );

        Toast.makeText(
                this,
                "Ký văn bản thành công",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void verifySignature() {

        if (rsa.getN() == 0) {
            Toast.makeText(
                    this,
                    "Hãy sinh khóa trước",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String input = edtSignatureInput
                .getText()
                .toString()
                .trim();

        if (input.isEmpty()) {
            Toast.makeText(
                    this,
                    "Hãy nhập chữ ký",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        try {

            String[] values = input.split("\\s+");

            StringBuilder result = new StringBuilder();

            for (String value : values) {

                long signature = Long.parseLong(value);

                long message = rsa.verify(signature);

                result.append(
                        (char) message
                );
            }

            txtVerifyResult.setText(
                    result.toString()
            );

            Toast.makeText(
                    this,
                    "Xác thực thành công",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Chữ ký không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private String convertListToString(List<Long> list) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            result.append(list.get(i));

            if (i < list.size() - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    private void randomPrime() {

        int[] primes = {
                11, 13, 17, 19, 23,
                29, 31, 37, 41, 43,
                47, 53, 59, 61, 67,
                71, 73, 79, 83, 89
        };

        int indexP =
                (int) (Math.random() * primes.length);

        int indexQ =
                (int) (Math.random() * primes.length);

        while (indexP == indexQ) {
            indexQ =
                    (int) (Math.random() * primes.length);
        }

        edtP.setText(
                String.valueOf(primes[indexP])
        );

        edtQ.setText(
                String.valueOf(primes[indexQ])
        );
    }
}