package com.example.chukyso;

public class RSA {

    private long p;
    private long q;
    private long n;
    private long phi;
    private long privateKey;
    private long publicKey;

    public void generateKeys(long p, long q) {
        this.p = p;
        this.q = q;

        n = p * q;
        phi = (p - 1) * (q - 1);

        privateKey = findE(phi);
        publicKey = modInverse(privateKey, phi);
    }

    private long findE(long phi) {
        long e = 11;

        if (gcd(e, phi) == 1) {
            return e;
        }

        return -1;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = a % b;
            a = b;
            b = temp;
        }

        return a;
    }

    private long modInverse(long a, long m) {
        for (long d = 1; d < m; d++) {
            if ((a * d) % m == 1) {
                return d;
            }
        }

        return -1;
    }

    public long modPow(long base, long exponent, long modulus) {
        long result = 1;

        base = base % modulus;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }

            base = (base * base) % modulus;
            exponent = exponent / 2;
        }

        return result;
    }

    public long sign(long message) {
        return modPow(message, privateKey, n);
    }

    public long verify(long signature) {
        return modPow(signature, publicKey, n);
    }

    public long getP() {
        return p;
    }

    public long getQ() {
        return q;
    }

    public long getN() {
        return n;
    }

    public long getPhi() {
        return phi;
    }

    public long getPrivateKey() {
        return privateKey;
    }

    public long getPublicKey() {
        return publicKey;
    }
}