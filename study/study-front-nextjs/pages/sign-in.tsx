import React, { useState } from 'react';
import Router, {NextRouter} from 'next/router';
import authApi from './api/authApi';
import GoogleLogin, { GoogleLoginResponse, GoogleLoginResponseOffline } from 'react-google-login';

const SignIn : React.FC = () => {
    const [userId, setUserId] = useState<string>('');
    const [userPassword, setUserPassword] = useState<string>('');

    const signIn = () => {
        console.log('userId, userPassword', userId, userPassword);
        const bodyData = {
            userId: userId,
            userPassword: userPassword,
        };

        authApi.signIn(bodyData)
            .then(res => {
                console.log('res', res);
            })
            .catch(e => {
                console.error('error', e);
            });
    }

    const handleSuccessOauth2 = (res: GoogleLoginResponse | GoogleLoginResponseOffline) => {
        let bodyData = {};
        if ('profileObj' in res && 'tokenObj' in res) {
            bodyData = {
                provider: 'google',
                providerId: res.profileObj.googleId,
                profileObj: res.profileObj,
                tokenObj: res.tokenObj,
            };
        }

        authApi.oauth2SignIn(bodyData)
            .then(response => {
                console.log(response);
            })
            .catch(error => {
                console.log(error);
            })
        ;
    }

    const handleFailureOauth2 = (res: any) => {
        console.log("handleFailureOauth2 ");
        console.log(res);
    }

    return (
        <>
            <div>
                <input onChange={e => setUserId(e.target.value)} placeholder={"ID"}/>
                <input onChange={e => setUserPassword(e.target.value)} placeholder={"PASSWORD"}/>
                <button onClick={() => signIn()}>로그인</button>
                <button onClick={() => Router.push('/sign-up')}>회원가입</button>
            </div>
            <div>
                {/*<button onClick={() => navigate('/oauth2/authorization/google')}>구글로그인</button>*/}
                <GoogleLogin clientId={'315991187398-usl0a6gtmvqrke2fdile5pb5rdtb4f26.apps.googleusercontent.com'}
                             onSuccess={res => handleSuccessOauth2(res)}
                             onFailure={res => handleFailureOauth2(res)}
                             redirectUri={'/'}
                             cookiePolicy={'single_host_origin'}
                />
            </div>
        </>
    );
};

export default SignIn;