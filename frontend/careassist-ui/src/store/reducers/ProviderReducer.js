const initialState = {
    providerClaims: [],
    providerProfile: {}
}

const ProviderReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'FETCH_CLAIMS_PROVIDER':
            return {
                ...state,
                providerClaims: action.payload
            }
        case 'ADD_CLAIM':
            return {
                ...state,
                providerClaims: [...state.providerClaims, action.payload]
            }
        case 'FETCH_PROVIDER_PROFILE':
            return {
                ...state,
                providerProfile: action.payload
            }
        case 'PUT_PROFILE':
            return {
                ...state,
                providerProfile: {
                    ...state.providerProfile,
                    ...action.payload
                }
            }
        default:
            return state
    }
}

export default ProviderReducer