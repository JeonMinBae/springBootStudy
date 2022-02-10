import { Action } from 'redux';

function createAction<T, P extends (...args: any) => any> (
    type: T,
    payloadCreator: P
): (...args: Parameters<P>) => Action<T> & {payload: ReturnType<P>};

function createAction<T>(type: T): () => Action<T>;
function createAction(type: any, payloadCreator?: any){
    return (...args: any[]) => ({
        type,
        ...(payloadCreator && {payload: payloadCreator(...args)})
    });
}

export default createAction;