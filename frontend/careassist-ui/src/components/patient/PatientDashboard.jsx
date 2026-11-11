
import { Outlet, useNavigate, useParams } from "react-router"
import PatientNavbar from "./PatientNavbar"
import { API_SERVER_URL_DEV } from "../../../config"
import axios from "axios"
import { useEffect, useState } from "react"

function PatientDashboard() {

    const navigate = useNavigate()
    const params = useParams()

    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const urlToken = params.token
        const memToken = localStorage.getItem('token')
        if (urlToken !== memToken) {
            navigate("/no-access")
        }

        const getUserDetails = async () => {
            try {
                const resp = await axios.get(API_SERVER_URL_DEV + "/api/auth/user-details", {
                    headers: { Authorization: "Bearer " + localStorage.getItem("token") }
                })

                if (resp.data.role !== "PATIENT") {
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

        <div className="bg-body-tertiary min-vh-100">
            <div className="container py-3">
                <PatientNavbar />
                <div className="mt-4">
                    < Outlet />
                </div>
            </div>
        </div>
    )
}

export default PatientDashboard
