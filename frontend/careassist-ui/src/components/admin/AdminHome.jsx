import axios from 'axios';
import React, { useState } from 'react';
import { API_SERVER_URL_DEV } from '../../../config';

function AdminHome() {
    const [patientId, setPatientId] = useState('');
    const [providerId, setProviderId] = useState('');
    const [insurerUsername, setInsurerUsername] = useState('');
    const [insurerPassword, setInsurerPassword] = useState('');
    const [providerName, setProviderName] = useState('');
    const [providerUsername, setProviderUsername] = useState('');
    const [providerPassword, setProviderPassword] = useState('');

    const handleDeactivatePatient = async(e) => {
        e.preventDefault();
        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/patient/deactive/${patientId}`,{},{
            headers : { Authorization : 'Bearer ' + localStorage.getItem('token')}
        })
        alert("Patient Has been Deactivated!!")
        } catch (err) {
            alert(err?.response?.data?.message)
        }
    };

    const handleDeactivateProvider = async(e) => {
        e.preventDefault();
        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/healthcareprovider/deactive/${providerId}`,{},{
            headers : { Authorization : 'Bearer ' + localStorage.getItem('token')}
        })
        alert("Provider Has been Deactivated!!")
        } catch (err) {
            alert(err?.response?.data?.message)
        }
        
    };

    const handleAddInsurer = async(e) => {
        e.preventDefault();
        let obj = {
            "username" : insurerUsername,
            "password" : insurerPassword
        }

        try {
            await axios.post(API_SERVER_URL_DEV + '/api/insurer/add', obj ,{
                headers : {Authorization : 'Bearer ' + localStorage.getItem('token')}
            })

            alert("insurer Added Successfully!")
        } catch (err) {
            alert(err)
        }
    };

    const handleAddProvider = async(e) => {
        e.preventDefault();
        let obj = {
            "name" : providerName,
            "username" : providerUsername,
            "password" : providerPassword
        }
        try {
            await axios.post(API_SERVER_URL_DEV + '/api/healthcareprovider/add', obj ,{
                headers : {Authorization : 'Bearer ' + localStorage.getItem('token')}
            })

            alert("Provider Added Successfully!")
        } catch (err) {
            alert(err)
        }
    };

    return (
        <div className="container py-4">
            <div className="d-flex align-items-center mb-4">
                <h2 className="fw-bold text-dark mb-0">Admin Control Dashboard</h2>
            </div>

            <div className="card shadow-sm border-0 mb-4">
                <div className="card-body p-4">
                    <h4 className="card-title text-danger mb-3">User Deactivation Management</h4>
                    <div className="row g-4">
                        <div className="col-md-6">
                            <div className="p-3 border rounded bg-light">
                                <h5 className="h6 fw-semibold mb-2">Deactivate Patient Account</h5>
                                <p className="text-muted small mb-3">Enter the unique patient ID to revoke system access.</p>
                                <form onSubmit={handleDeactivatePatient} className="input-group">
                                    <input type="number" className="form-control" placeholder="Enter Patient ID" 
                                        value={patientId} onChange={(e) => setPatientId(e.target.value)} required 
                                    />
                                    <button type="submit" className="btn btn-danger">Deactivate</button>
                                </form>
                            </div>
                        </div>
                        <div className="col-md-6">
                            <div className="p-3 border rounded bg-light">
                                <h5 className="h6 fw-semibold mb-2">Deactivate Healthcare Provider</h5>
                                <p className="text-muted small mb-3">Enter the provider ID to suspend hospital/clinic access.</p>
                                <form onSubmit={handleDeactivateProvider} className="input-group">
                                    <input type="number" className="form-control" placeholder="Enter Provider ID" 
                                        value={providerId} onChange={(e) => setProviderId(e.target.value)} required 
                                    />
                                    <button type="submit" className="btn btn-danger">Deactivate</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <hr className="my-4 text-muted" />
            <div className="card shadow-sm border-0">
                <div className="card-body p-4">
                    <h4 className="card-title text-primary mb-3">Partner Onboarding Hub</h4>
                    <div className="row g-4">
                        <div className="col-md-6">
                            <div className="p-3 border rounded bg-light h-100 d-flex flex-column justify-content-between">
                                <div>
                                    <h5 className="h6 fw-semibold mb-3">Register New Insurer</h5>
                                    <form onSubmit={handleAddInsurer} id="insurerForm">
                                        <div className="mb-3">
                                            <label className="form-label small fw-bold">Username</label>
                                            <input type="text" className="form-control" placeholder="Enter username"
                                                value={insurerUsername} onChange={(e) => setInsurerUsername(e.target.value)} required 
                                            />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label small fw-bold">Password</label>
                                            <input type="password" className="form-control" placeholder="Enter secure password"
                                                value={insurerPassword} onChange={(e) => setInsurerPassword(e.target.value)} required 
                                            />
                                        </div>
                                    </form>
                                </div>
                                <button type="submit" form="insurerForm" className="btn btn-primary w-100 mt-2">+ Add Insurer</button>
                            </div>
                        </div>
                        <div className="col-md-6">
                            <div className="p-3 border rounded bg-light h-100 d-flex flex-column justify-content-between">
                                <div>
                                    <h5 className="h6 fw-semibold mb-3">Register New Healthcare Provider</h5>
                                    <form onSubmit={handleAddProvider} id="providerForm">
                                        <div className="mb-3">
                                            <label className="form-label small fw-bold">Hospital / Clinic Name</label>
                                            <input type="text" className="form-control" placeholder="Enter facility name"
                                                value={providerName} onChange={(e) => setProviderName(e.target.value)} required 
                                            />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label small fw-bold">Username</label>
                                            <input type="text" className="form-control" placeholder="Enter username"
                                                value={providerUsername} onChange={(e) => setProviderUsername(e.target.value)} required 
                                            />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label small fw-bold">Password</label>
                                            <input type="password" className="form-control" placeholder="Enter secure password"
                                                value={providerPassword} onChange={(e) => setProviderPassword(e.target.value)} required 
                                            />
                                        </div>
                                    </form>
                                </div>
                                <button type="submit" form="providerForm" className="btn btn-primary w-100 mt-2">+ Add Provider</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminHome;