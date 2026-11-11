import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"

export const getClaims = () => async (dispatch) => {
    const response = await axios.get(`${API_SERVER_URL_DEV}/api/healthcareprovider/claim/all`,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }
    )
    dispatch({
        type: 'FETCH_CLAIMS_PROVIDER',
        payload: response.data
    })
}

export const addClaim = (policyId, claim) => async (dispatch) => {
    const response = await axios.post(`${API_SERVER_URL_DEV}/api/claim/add/cashless/${policyId}`, claim,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }
    )
    dispatch({
        type: 'ADD_CLAIM',
        payload: response.data
    })
}

export const getProfile = () => async (dispatch) => {
    const response = await axios.get(`${API_SERVER_URL_DEV}/api/healthcareprovider/view`,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }
    )
    dispatch({
        type: 'FETCH_PROVIDER_PROFILE',
        payload: response.data
    })
}

export const putProfile = (obj) => async (dispatch) => {
    const response = await axios.put(API_SERVER_URL_DEV + "/api/healthcareprovider/update", obj, {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
    })
    dispatch({
        type: 'PUT_PROFILE',
        payload: obj
    })
}