
import { useDispatch, useSelector } from "react-redux"
import { putProfile } from "../../store/actions/ProviderAction"
import { useEffect, useState } from "react"

function ProviderProfile() {


    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")
    const [name,setName] = useState("")

    const profile = useSelector(state => state.providerProfile)
    // console.log(profile)
    const dispatch = useDispatch()

    useEffect(() => {
    if (profile?.name) {
        setName(profile.name)
    }
}, [profile])

    const onSave = async (e) => {
        e.preventDefault()

        if (!name.trim()) {
            setErrMsg("Name is required")
            return
        }

        let obj = {
            "name": name.trim()
        }
        try {
            await dispatch(putProfile(obj))
            alert("Profile updated successfully!")
        }
        catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)

        }

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
                    <h4 className="mt-3 mb-1 fw-bold">Provider Profile</h4>
                    <p className="text-muted mb-0 small">Manage your hospital details</p>
                </div>
                {/* <!-- Error / Header --> */}
                <div className="px-3">
                    <p className="text-danger fw-semibold mb-0">
                        {errMsg}
                    </p>
                    <p className="text-muted small mb-0">
                        {errComment}
                    </p>
                </div>
                <div className="card-body p-4">
                    <div className="mb-4">
                        <label className="form-label fw-semibold">
                            Hospital / clinic name
                        </label>
                        <div className="input-group input-group-lg">
                            <span className="input-group-text bg-white"><i className="bi bi-hospital text-primary"></i></span>
                            <input className="form-control" type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter your name" />
                        </div>
                        <div className="form-text">This name appears on claims and invoices.</div>
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

export default ProviderProfile