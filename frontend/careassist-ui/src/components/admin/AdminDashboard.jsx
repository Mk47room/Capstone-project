import axios from "axios";
import { useEffect } from "react"
import { Outlet, useNavigate, useParams } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config";
import AdminNavbar from "./AdminNavbar";

function AdminDashboard() {

    const navigate = useNavigate()
    let params = useParams();

    const userDetailsUrl = API_SERVER_URL_DEV + '/api/auth/user-details'

    useEffect(() => {
        // console.log('in use effect....')
        const urlToken = params.token
        const memToken = localStorage.getItem('token')
        if (urlToken !== memToken) {
            navigate("/no-access")
        }
        // console.log(`tokens match: ${urlToken === memToken}`)

        const getUserDetails = async () => {
            // console.log('in user details Fn')

            try {
                // Step 2. Fetch User Details using memToken
                const resp = await axios.get(userDetailsUrl, {
                    headers: { 'Authorization': 'Bearer ' + memToken }
                })
                // console.log(resp)

                if (!(resp.data.role === 'ADMIN')) {
                    navigate("/no-access")
                }

            }
            catch (err) {
                navigate("/no-access")
            }

        }
        getUserDetails()

    }, [params])
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-md-12">
                    <AdminNavbar />
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

export default AdminDashboard