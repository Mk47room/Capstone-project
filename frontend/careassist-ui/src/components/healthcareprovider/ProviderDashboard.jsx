import { Outlet, useNavigate, useParams } from "react-router";
import { API_SERVER_URL_DEV } from "../../../config";
import { useEffect } from "react";
import axios from "axios";
import ProviderNavbar from "./ProviderNavbar";
import { useDispatch } from "react-redux";
import { getClaims, getProfile } from "../../store/actions/ProviderAction";

function ProviderDashboard() {
    const navigate = useNavigate()
    const dispatch = useDispatch()
    let params = useParams();

    const userDetailsUrl = API_SERVER_URL_DEV + '/api/auth/user-details'

    useEffect(() => {
        const urlToken = params.token
        const memToken = localStorage.getItem('token')
        if (urlToken !== memToken) {
            navigate("/no-access")
        }

        const getUserDetails = async () => {
            try {
                const resp = await axios.get(userDetailsUrl, {
                    headers: { 'Authorization': 'Bearer ' + memToken }
                })

                if (!(resp.data.role === 'HEALTHCAREPROVIDER')) {
                    navigate("/no-access")
                }
            }
            catch (err) {
                navigate("/no-access")
            }

        }
        getUserDetails()

        dispatch(getClaims())
        dispatch(getProfile())

    }, [params])

    return (
        <div className="bg-body-tertiary min-vh-100">
            <div className="container py-3">
                <ProviderNavbar />
                <div className="mt-4">
                    <Outlet />
                </div>
            </div>
        </div>
    )
}
export default ProviderDashboard