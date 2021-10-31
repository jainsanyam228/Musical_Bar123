#include<bits/stdc++.h>
#include<string>
#include <algorithm>
#include <set>
#include <cmath>
#include <vector>
#define ll long long
#define repi(i,a,b) for(ll i=a;i<=b;i++)
#define repd(i,a,b) for(ll i=a;i>=b;i--)
#define cy cout<<"YES\n"
#define cn cout<<"NO\n"
#define MOD 1000000007
#define vi vector<int>
#define vl vector<ll>
#define pb push_back
#define nl cout<<"\n"
#define cm cout<<"-1\n"
using namespace std;

ll gcd(ll a, ll h)
{
   ll temp;
   while (1){
        temp = a%h;
        if (temp == 0) return h;
        a = h;
        h = temp;
    }
}

int main()
{
    double x = 3;
    double y = 7;
    double t = 2;
    double n = x*y;
    double phi = (x-1)*(y-1);
    while (t < phi){
        if (gcd(t, phi)==1) break;
        else t++;
    }
    int k = 2;
    double d = (1 + (k*phi))/t;
    double msg = 12;
    cout <<"Message data: "<< msg;
    double c = pow(msg, t);
    c = fmod(c, n);
    cout << "\nEncrypted data: " << c;
    double m = pow(c, d);
    m = fmod(m, n);
    cout << "\nOriginal Message Sent: " << m;
return 0;
}