import { useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { addClaim } from "../../store/actions/ProviderAction"
import StatusBadge from "../common/StatusBatch"

function ClaimList() {

    const [policyId, setPolicyId] = useState("")
    const [diagnosis, setDiagnosis] = useState("")
    const [treatment, setTreatment] = useState("")
    const [claimedAmount, setClaimedAmount] = useState("")
    const [admissionDate, setAdmissionDate] = useState("")

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const list = useSelector(state => state.providerClaims)
    // console.log(list)
    const dispatch = useDispatch()

    const onAddClaim = async (e) => {
        e.preventDefault()

        let claim = {
            'diagnosis': diagnosis,
            'treatment': treatment,
            'claimedAmount': claimedAmount,
            'admissionDate': admissionDate
        }
        if (!policyId.trim()) {
            setErrMsg("Policy ID is required")
            return
        }
        try {
            await dispatch(addClaim(policyId, claim))
            alert("Claim added successfully")
        }
        catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)

        }

    }

    return (

        <div>
            <div className="card border-0 shadow-sm">

                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">My claims</h5>
                    <button
                        className="btn btn-primary btn-sm" onClick={() => {
                            setPolicyId("")
                            setDiagnosis("")
                            setTreatment("")
                            setClaimedAmount("")
                            setAdmissionDate("")
                            setErrMsg("")
                            setErrComment("")
                        }} data-bs-toggle="modal" data-bs-target="#claimModal"
                    ><i className="bi bi-plus-lg me-1"></i>Add claim</button>
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

                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Claim ID</th>
                                <th>Status</th>
                                <th>Diagnosis</th>
                                <th className="text-end">Claimed (₹)</th>
                                <th className="text-end">Approved (₹)</th>
                                <th>Submitted Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {list?.map((c, index) => (
                                <tr key={c.claimId}>
                                    <th>{index + 1}</th>
                                    <td className="fw-semibold">{c.claimId}</td>
                                    <td><StatusBadge status={c.status} /></td>
                                    <td>{c.diagnosis}</td>
                                    <td className="text-end">{c.claimedAmount}</td>
                                    <td className="text-end">{c.approvedAmount}</td>
                                    <td>{c.submittedDate?.split("T")[0]}</td>
                                </tr>
                            ))}

                        </tbody>
                    </table>
                </div>
            </div>

            <div className="modal fade" id="claimModal" data-bs-backdrop="static" data-bs-keyboard="false"
                tabIndex="-1" aria-labelledby="claimModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content border-0 shadow">
                        <div className="modal-header">
                            <h5 className="modal-title" id="claimModalLabel">
                                New cashless request
                            </h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />
                        </div>
                        <div className="modal-body">
                            <div className="mb-3">
                                <label className="form-label">Policy ID</label>
                                <input type="number" value={policyId} onChange={(e) => setPolicyId(e.target.value)} className="form-control" />
                                <div className="form-text">The patient's policy number.</div>
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Diagnosis</label>
                                <input type="text" value={diagnosis} onChange={(e) => setDiagnosis(e.target.value)} className="form-control" />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Treatment</label>
                                <input type="text" value={treatment} onChange={(e) => setTreatment(e.target.value)} className="form-control" />
                            </div>
                            <div className="row">
                                <div className="col-md-6 mb-3">
                                    <label className="form-label">Estimated cost (₹)</label>
                                    <input type="number" value={claimedAmount} onChange={(e) => setClaimedAmount(e.target.value)} className="form-control" />
                                </div>
                                <div className="col-md-6 mb-3">
                                    <label className="form-label">Admission date</label>
                                    <input type="date" value={admissionDate} onChange={(e) => setAdmissionDate(e.target.value)} className="form-control" />
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-light" data-bs-dismiss="modal"
                            > Close</button>

                            <button type="button" className="btn btn-primary" onClick={onAddClaim} data-bs-dismiss="modal">
                                Submit request
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}

export default ClaimList