// 뒤에 as const 를 붙여줌으로써 나중에 액션 객체를 만들게 action.type 의 값을 추론하는 과정에서
// action.type 이 string 으로 추론되지 않고 'counter/INCREASE' 와 같이 실제 문자열 값으로 추론 되도록 해줍니다.
// import createAction from './createAction';
// import handleActions from './handleActions';
// import { Action } from 'redux';
import { ActionType, createAction, createReducer } from 'typesafe-actions';

const SET_ACCESS_TOKEN = 'token/SET_ACCESS_TOKEN' as const;
const SET_REFRESH_TOKEN = 'token/SET_REFRESH_TOKEN' as const;

export const setAccessToken =
    createAction(SET_ACCESS_TOKEN, (token : string)  => token)();
export const setRefreshToken =
    createAction(SET_REFRESH_TOKEN, (token : string)  => token)();

type TokenState = {
    accessToken: string,
    refreshToken: string,
}

const initialState: TokenState = {
    accessToken: '',
    refreshToken: '',
}

const actions = {setAccessToken, setRefreshToken}
type TokenAction = ActionType<typeof actions>;

const tokenReducer = createReducer<TokenState, TokenAction>(initialState,{
    [SET_ACCESS_TOKEN]: (state, {payload: token}) => ({
        ...state,
        accessToken: token,
    }),
    [SET_REFRESH_TOKEN]: (state, {payload: token}) => ({
        ...state,
        refreshToken: token,
    }),
});


// const tokenReducer = handleActions<TokenAction, TokenPayload>({
//         [SET_ACCESS_TOKEN]: (state, action) => ({
//             ...state,
//             accessToken: action.payload.token,
//         }),
//         [SET_REFRESH_TOKEN]: (state, { payload: token }) => ({
//             ...state,
//             refreshToken: token,
//         }),
//
//     },
//     initialState,
// );

export default tokenReducer;