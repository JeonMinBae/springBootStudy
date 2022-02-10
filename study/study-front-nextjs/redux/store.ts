import { combineReducers, createStore } from 'redux';
import tokenReducer from "./token";

const rootReducer = combineReducers({
    token: tokenReducer,
});
export type RootState = ReturnType<typeof rootReducer>;

const store = createStore(rootReducer);
export default store;
