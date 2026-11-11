import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { useNavigate } from "react-router"
import { useEffect, useState } from "react"

function PatientProfile() {

    const navigate = useNavigate()

    const riskCategoryValues = ['CHRONIC_ILLNESS', 'PRE_EXISTING', 'ACUTE_SYMPTOMS', 'NONE']
    const [name, setName] = useState("")
    const [dob, setDob] = useState("")
    const [contact, setContact] = useState("")
    const [riskCategory, setRiskCategory] = useState("")
    const [chronicIllness, setChronicIllness] = useState("")
    const [preExisting, setPreExisting] = useState("")
    const [acuteSymptoms, setAcuteSymptoms] = useState("")

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    useEffect(() => {
        const getDetails = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + "/api/patient/view", {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                setName(response.data.name)
                setDob(response.data.dob)
                setContact(response.data.contact)
                setRiskCategory(response.data.riskCategory)
                setChronicIllness(response.data.chronicIllness)
                setPreExisting(response.data.preExisting)
                setAcuteSymptoms(response.data.acuteSymptoms)

            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
            }

        }

        getDetails()
    }, [])



    const onSave = async () => {
        let obj = {
            "name": (name ?? "").trim(),
            "contact": (contact ?? "").trim(),
            "riskCategory": (riskCategory ?? "").trim(),
            "chronicIllness": (chronicIllness ?? "").trim(),
            "preExisting": (preExisting ?? "").trim(),
            "acuteSymptoms": (acuteSymptoms ?? "").trim()
        };
        await axios.put(API_SERVER_URL_DEV + "/api/patient/update", obj, {
            headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        })
        alert("Profile updated successfully!")
    }


    return (
        <div className="container py-4">

            <div className="card border-0 shadow-sm mx-auto overflow-hidden" style={{ maxWidth: "650px" }}>
                <div className="card-header bg-primary-subtle text-center py-4 border-0">
                    <img src="/uploads/provider/NoProfile.jpg" alt="Provider Profile" className="rounded-circle border border-4 border-white shadow"
                        style={{
                            width: "130px",
                            height: "130px",
                            objectFit: "cover"
                        }}
                    />
                    <h4 className="mt-3 mb-1 fw-bold">Patient Profile</h4>
                    <p className="text-muted mb-0 small">Manage your details</p>
                </div>

                <div className="card-body p-4">
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Name</label>
                        <div className="input-group input-group-lg">
                            <input className="form-control" type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter your Name" />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">DOB</label>
                        <div className="input-group input-group-lg">
                            <input className="form-control" type="date" value={dob} disabled />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Contact</label>
                        <div className="input-group input-group-lg">
                            <input className="form-control" type="number" value={contact} onChange={(e) => setContact(e.target.value)} placeholder="Enter your contact" />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Risk Category</label>
                        <div className="input-group input-group-lg">
                            <select className="form-select form-select-lg" required
                                value={riskCategory} onChange={(e) => setRiskCategory(e.target.value)}>
                                {riskCategoryValues.map((val) => (
                                    <option key={val} value={val}>{val}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Chronic Illness</label>
                        <div className="input-group input-group-lg">
                            <textarea className="form-control form-control-lg"
                                value={chronicIllness ?? ""} onChange={(e) => setChronicIllness(e.target.value)} />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Pre Existing</label>
                        <div className="input-group input-group-lg">
                            <textarea className="form-control form-control-lg"
                                value={preExisting ?? ""} onChange={(e) => setPreExisting(e.target.value)} />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Acute Symptoms</label>
                        <div className="input-group input-group-lg">
                            <textarea className="form-control form-control-lg"
                                value={acuteSymptoms ?? ""} onChange={(e) => setAcuteSymptoms(e.target.value)} />
                        </div>
                    </div>
                    <div className="d-flex justify-content-end">
                        <button className="btn btn-primary px-4" onClick={onSave}>
                            <i className="bi bi-save me-2"></i>Save changes
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default PatientProfile