import axios from "axios"
import { useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { API_SERVER_URL_DEV } from "../../../config"

function Invoice() {

    const [claimId, setClaimId] = useState("")
    const [consultationFee, setConsultationFee] = useState("")
    const [diagnosticsFee, setDiagnosticsFee] = useState("")
    const [pharmacyFee, setPharmacyFee] = useState("")
    const [roomCharges, setRoomCharges] = useState("")

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const list = useSelector(state => state.providerClaims)
    let temp = [...list.filter(c => c.status === 'APPROVED')]

    const onEdit = (c) => {
        setClaimId(c.claimId)
    }

    const obj = {
        'consultationFee' : consultationFee,
        'diagnosticsFee' : diagnosticsFee,
        'pharmacyFee' : pharmacyFee,
        'roomCharges' : roomCharges
    }

    const onCreate = async() =>{
        try {
            await axios.post(`${API_SERVER_URL_DEV}/api/invoice/add/${claimId}`,obj,{
            headers : {Authorization : 'Bearer '+ localStorage.getItem('token')}
        })
        alert("Invoice Created Successfully!")
        } 
        catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)
        }
        
    }


    return (
        <div>
            <div>
                <h4>Invoice</h4>
                <hr />
                <button
                    className="btn btn-primary" onClick={() => {
                        setClaimId("")
                        setConsultationFee("")
                        setDiagnosticsFee("")
                        setPharmacyFee("")
                        setRoomCharges("")
                        setErrMsg("")
                        setErrComment("")
                    }} data-bs-toggle="modal" data-bs-target="#exampleModal"
                >+ New Invoice</button>
            </div>
            {/* <!-- Error / Header --> */}
                <div className="text-center mb-4">

                    <p style={{ color: 'red', fontSize: '1.2rem' }}>
                        {errMsg}
                    </p>
                    <p className="text-muted small">
                        {errComment}
                    </p>
                </div>
            <div>
                <div>
                    <div className="card shadow-sm">
                        <div className="card-header d-flex justify-content-between align-items-center">
                            <h5 className="mb-0">Approved Claim Details</h5>
                        </div>

                        <div className="card-body">
                            <div className="table-responsive">
                                <table className="table table-striped table-hover align-middle mb-0">
                                    <thead className="table-light">
                                        <tr>
                                            <th>S.No</th>
                                            <th>Claim ID</th>
                                            <th>Status</th>
                                            <th>Diagnosis</th>
                                            <th>Claimed Amount</th>
                                            <th>Approved Amount</th>
                                            <th>Submitted Date</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {temp?.map((c, index) => (
                                            <tr key={index}>
                                                <th>{index + 1}</th>
                                                <td>{c.claimId}</td>
                                                <td>{c.status}</td>
                                                <td>{c.diagnosis}</td>
                                                <td>{c.claimedAmount}</td>
                                                <td>{c.approvedAmount}</td>
                                                <td>{c.submittedDate?.split("T")[0]}</td>
                                                <td>
                                                    <button className="btn btn-info" onClick={() => onEdit(c)} data-bs-toggle="modal" data-bs-target="#exampleModal">Create Invoice</button>
                                                </td>
                                            </tr>
                                        ))}

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <div className="modal fade" id="exampleModal" data-bs-backdrop="static" data-bs-keyboard="false"
                tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">
                                Invoice Details
                            </h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />
                        </div>
                        <div className="modal-body">
                            <div className="mb-3">
                                <label className="form-label">Claim ID</label>
                                <input type="number" value={claimId} onChange={(e) => setClaimId(e.target.value)} className="form-control" />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Consultation Fee</label>
                                <input type="number" value={consultationFee} onChange={(e) => setConsultationFee(e.target.value)} className="form-control" />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Diagnostics Fee</label>
                                <input type="number" value={diagnosticsFee} onChange={(e) => setDiagnosticsFee(e.target.value)} className="form-control" />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Pharmacy Fee</label>
                                <input type="number" value={pharmacyFee} onChange={(e) => setPharmacyFee(e.target.value)} className="form-control" />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Room Charges</label>
                                <input type="number" value={roomCharges} onChange={(e) => setRoomCharges(e.target.value)} className="form-control" />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal"
                            > Cancel</button>

                            <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={()=>onCreate()}>
                                Create Invoice
                            </button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}
export default Invoice