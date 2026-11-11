import { Outlet, useNavigate, useParams } from "react-router";
import { API_SERVER_URL_DEV } from "../../../config";
import { useEffect } from "react";
import axios from "axios";
import InsurerNavbar from "./InsurerNavbar";

function InsurerDashboard(){
    const navigate = useNavigate()
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

                if (!(resp.data.role === 'INSURER')) {
                    navigate("/no-access")
                }

            }
            catch (err) {
                navigate("/no-access")
            }

        }
        getUserDetails()

    }, [params])

    return(
        <div className="container-fluid">
            <div className="row">
                <div className="col-md-12">
                    <InsurerNavbar />
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <Outlet />
                </div>
            </div>
        </div>
    )
}
export default InsurerDashboard