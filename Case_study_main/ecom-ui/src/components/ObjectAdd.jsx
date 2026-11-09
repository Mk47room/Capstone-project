import axios from "axios"
import { useState } from "react"
import { Link } from "react-router"

function ObjectAdd() {

    const [name, setName] = useState("")
    const [year, setYear] = useState("")
    const [price, setPrice] = useState("")
    const [cpu, setCpu] = useState("")


    const onAdd = async (e) => {
        e.preventDefault()

        let obj = {
            "name": name,
            "data": {
                "Year": year,
                "Price": price,
                "CPU": cpu
            }
        }

        try {
            const response = await axios.post(`https://api.restful-api.dev/objects`, obj)
            alert("Added Object with Id: " + response.data.id)
        } catch (err) {
            console.log(err)
        }
    }

    return (
        <div className="container mt-5" style={{ maxWidth: "600px" }}>
            <div className="card shadow-sm border-0">
                <div className="card-body p-4">
                    <div className="border-bottom pb-3 mb-4">
                        <h2 className="h4 fw-bold text-dark mb-1">Add New Product</h2>
                    </div>
                        <div className="mb-3">
                            <label className="form-label fw-semibold">Product Name</label><br/>
                            <input type="text" className="form-control" value={name}
                                onChange={(e) => setName(e.target.value)} required />
                        </div>
                        <div className="row">
                            <div className="col-md-6 mb-3">
                                <label className="form-label fw-semibold">Year</label><br/>
                                <input type="number" className="form-control"
                                    value={year} onChange={(e) => setYear(e.target.value)} required />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="form-label fw-semibold">Price</label><br/>
                                <input type="number" className="form-control"
                                    value={price} onChange={(e) => setPrice(e.target.value)} required />
                            </div>
                        </div>
                        <div className="mb-4">
                            <label className="form-label fw-semibold">CPU Model</label><br/>
                            <input type="text" className="form-control" value={cpu}
                                onChange={(e) => setCpu(e.target.value)} required />
                        </div>
                        <div className="d-flex justify-content-end gap-2 pt-2">
                            <Link to="/" className="btn btn-outline-secondary px-4">Cancel</Link> &nbsp;&nbsp; 
                            <button type="submit" className="btn btn-primary px-4" onClick={onAdd}>Save Object</button>
                        </div>
                </div>
            </div>
        </div>
    )
}
export default ObjectAdd