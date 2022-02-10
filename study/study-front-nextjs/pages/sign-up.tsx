import React from 'react';
import {useForm} from 'react-hook-form';
import authApi from './api/authApi';
import Router from 'next/router';

interface IFormInputs {
    userId : string,
    userPassword : string,
    userName : string,
    userNickName : string,
    userEmail : string,
    userPhoneNumber : string,
    userTelephone : string | null,
    userAddress : string,
    userZipcode : string,
    userAddressDetail : string | null,
}

const SignUp = () => {
    const defaultValues : IFormInputs = {
        userId : "hardy2",
        userPassword : "",
        userName : "justName",
        userNickName : "justNickName2",
        userEmail : "test@test.test",
        userPhoneNumber : "01012341234",
        userTelephone : "023451234",
        userAddress : "sdaf",
        userZipcode : "018232",
        userAddressDetail : "",
    };

    const {
        handleSubmit,
        control,
        register,
        setValue,
        getValues,
    } = useForm({defaultValues});

    const signUp = async (formData : IFormInputs) =>{
        const bodyData = {
            userId : formData.userId,
            userPassword : formData.userPassword,
            userName : formData.userName,
            userNickName : formData.userNickName,
            userEmail : formData.userEmail,
            userPhoneNumber : formData.userPhoneNumber,
            userTelephone : formData.userTelephone,
            userAddress : formData.userAddress,
            userZipcode : formData.userZipcode,
            userAddressDetail : formData.userAddressDetail,
        }
        const response = await authApi.signUp(bodyData);
        console.log('response', response);
        if(response.status == 201){
            Router.push('/sign-in');
        }else{
        }

    }

    return (
        <div>
            This is sign up page
            <form
                onSubmit={handleSubmit(signUp)}
            >
                <div>
                    <label>아이디: </label>
                    <input
                        {...register('userId', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>비밀번호: </label>
                    <input
                        {...register('userPassword', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>이름: </label>
                    <input
                        {...register('userName', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>닉네임: </label>
                    <input
                        {...register('userNickName', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>이메일: </label>
                    <input
                        {...register('userEmail', {required: true})}
                        type={"email"}
                    />
                </div>
                <div>
                    <label>휴대폰번호: </label>
                    <input
                        {...register('userPhoneNumber', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>집전화번호: </label>
                    <input
                        {...register('userTelephone', {})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>주소: </label>
                    <input
                        {...register('userAddress', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>우편번호: </label>
                    <input
                        {...register('userZipcode', {required: true})}
                        type={"text"}
                    />
                </div>
                <div>
                    <label>상세주소: </label>
                    <input
                        {...register('userAddressDetail', {})}
                        type={"text"}
                    />
                </div>
                <button type={"submit"}>회원가입</button>
            </form>

        </div>
    );
};

export default SignUp;