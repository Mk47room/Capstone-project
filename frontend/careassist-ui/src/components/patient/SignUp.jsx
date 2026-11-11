import { useState } from "react"
import { Link, useNavigate } from "react-router"
import "../../styles/SignUp.css"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"

function SignUp() {

    const riskCategoryValues = ['CHRONIC_ILLNESS', 'PRE_EXISTING', 'ACUTE_SYMPTOMS', 'NONE']
    const genderValues = ['MALE', 'FEMALE', 'OTHERS']
    const [name, setName] = useState("")
    const [dob, setDob] = useState("")
    const [gender, setGender] = useState("")
    const [contact, setContact] = useState("")
    const [riskCategory, setRiskCategory] = useState("")
    const [chronicIllness, setChronicIllness] = useState("")
    const [preExisting, setPreExisting] = useState("")
    const [acuteSymptoms, setAcuteSymptoms] = useState("")

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const navigate = useNavigate()


    const processSignup = async (e) => {
        e.preventDefault()

        let obj = {
            "name": name,
            "dob": dob,
            "gender": gender,
            "contact": contact,
            "riskCategory": riskCategory,
            "chronicIllness": chronicIllness,
            "preExisting": preExisting,
            "acuteSymptoms": acuteSymptoms,
            "username": username,
            "password": password
        }

        try {
            await axios.post(API_SERVER_URL_DEV + "/api/patient/add", obj)
            navigate('/')

        } catch (err) {
            setErrMsg(err?.response?.data?.message || err.message);
            setErrComment(err?.response?.data?.comment);
        }
    }


return (
    <div className="signup-page">
        <div className="container min-vh-100 d-flex align-items-center py-4">
            <div className="row justify-content-center w-100">
                <div className="col-12 col-sm-9 col-md-7 col-lg-5 col-xl-4">

                    {/* <!-- Signup Card --> */}
                    <div className="card border-0 rounded-4 p-4 p-sm-5 bg-white signup-card">
                        <div className="card-body p-0">

                            {/* <!-- Branding / Header --> */}
                            <div className="text-center mb-4">
                                <h1 className="h4 fw-bold mb-1 signup-title">Care Assist</h1>
                                <p className="text-muted small">Please enter your details to sign up.</p>
                            </div>

                            {/* <!-- Error / Header --> */}
                            <div className="text-center mb-4">
                                <p className="signup-error">{errMsg}</p>
                                <p className="text-muted small">
                                    {errComment}
                                </p>
                            </div>

                            {/* <!-- Signup Form --> */}
                            <form onSubmit={(e) => processSignup(e)}>

                                {/* <!-- Name Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Name</label>
                                    <input type="text" className="form-control form-control-lg" required
                                        value={name} onChange={(e) => setName(e.target.value)} />
                                </div>

                                {/* <!-- DOB Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">DOB</label>
                                    <input type="date" className="form-control form-control-lg" required
                                        max={new Date().toISOString().split("T")[0]}
                                        value={dob} onChange={(e) => setDob(e.target.value)} />
                                </div>

                                {/* <!-- Gender Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Gender</label>
                                    <select className="form-select form-select-lg" required
                                        value={gender} onChange={(e) => setGender(e.target.value)}>
                                        <option value="">----select Gender----</option>
                                        {genderValues.map((val) => (
                                            <option key={val} value={val}>{val}</option>
                                        ))}
                                    </select>
                                </div>

                                {/* <!-- Contact Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Contact</label>
                                    <input type="tel" className="form-control form-control-lg" required
                                        inputMode="numeric" pattern="[0-9]{10}" maxLength={10}
                                        placeholder="10-digit mobile number" title="Enter a 10-digit phone number"
                                        value={contact} onChange={(e) => setContact(e.target.value)} />
                                </div>

                                {/* <!-- Risk Category Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Risk Category</label>
                                    <select className="form-select form-select-lg" required
                                        value={riskCategory} onChange={(e) => setRiskCategory(e.target.value)}>
                                        <option value="">----select Category----</option>
                                        {riskCategoryValues.map((val) => (
                                            <option key={val} value={val}>{val}</option>
                                        ))}
                                    </select>
                                </div>

                                {/* <!-- Chronic Illness Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Chronic Illness</label>
                                    <textarea className="form-control form-control-lg"
                                        value={chronicIllness} onChange={(e) => setChronicIllness(e.target.value)} />
                                </div>

                                {/* <!-- Pre Existing Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Pre Existing</label>
                                    <textarea className="form-control form-control-lg"
                                        value={preExisting} onChange={(e) => setPreExisting(e.target.value)} />
                                </div>

                                {/* <!-- Acute Symptoms Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Acute Symptoms</label>
                                    <textarea className="form-control form-control-lg"
                                        value={acuteSymptoms} onChange={(e) => setAcuteSymptoms(e.target.value)} />
                                </div>

                                {/* <!-- Username Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Username</label>
                                    <input type="text" className="form-control form-control-lg" required
                                        value={username} onChange={(e) => setUsername(e.target.value)} />
                                </div>

                                {/* <!-- Password Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Password</label>
                                    <input type="password" className="form-control form-control-lg"
                                        placeholder="••••••••" required minLength={8}
                                        value={password} onChange={(e) => setPassword(e.target.value)} />
                                </div>

                                {/* <!-- Submit Button --> */}
                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary btn-lg fw-semibold signup-btn">
                                        Sign up
                                    </button>
                                </div>

                                {/* <!-- Footer Link --> */}
                                <div className="text-center">
                                    <p className="small text-muted mb-0">
                                        Have an account?{" "}
                                        <Link to="/" className="text-primary text-decoration-none fw-semibold signup-link">
                                            Login
                                        </Link>
                                    </p>
                                </div>
                            </form>

                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
)
}

export default SignUp