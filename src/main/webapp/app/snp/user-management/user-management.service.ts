import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

export interface UserRow {
  id: number;
  login: string | null;
  firstName: string | null;
  lastName: string | null;
  email: string | null;
  langKey: string | null;
  isActivated: boolean;
  status: 'active' | 'inactive' | string;
  profile: string[];
}

export interface UserPage {
  rows: UserRow[];
  totalRecords: number;
  totalPages: number;
}

export interface UserPayload {
  id?: number | null;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  activated: boolean;
  langKey: string;
  profiles: string[];
}

@Injectable({ providedIn: 'root' })
export class UserManagementService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/users`;

  getAllUserList(page: number, size: number): Observable<UserPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http
      .get<{ response?: { rows?: UserRow[]; totalRecords?: number; totalPages?: number } }>(`${this.resourceUrl}/all`, { params })
      .pipe(
        map(body => ({
          rows: body.response?.rows ?? [],
          totalRecords: body.response?.totalRecords ?? 0,
          totalPages: body.response?.totalPages ?? 0,
        })),
      );
  }

  getUserById(id: number): Observable<UserRow> {
    return this.http.get<{ success?: UserRow }>(`${this.resourceUrl}/id/${id}`).pipe(map(body => body.success as UserRow));
  }

  /** Authority names for the profile picker. */
  getProfiles(): Observable<string[]> {
    return this.http
      .get<{ success?: string[] }>(`${this.resourceUrl}/profiles`)
      .pipe(map(body => (Array.isArray(body.success) ? body.success : [])));
  }

  create(payload: UserPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/create`, payload);
  }

  update(payload: UserPayload): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/update`, payload);
  }

  setActive(id: number, active: boolean): Observable<unknown> {
    return this.http.patch(`${this.resourceUrl}/id/${id}/${active ? 'active' : 'inactive'}`, {});
  }

  /**
   * snp21 sent ?login= but the controller declared the parameter as "LOGin"
   * (a bad find/replace of log -> LOG), so this always failed. snp22 accepts "login".
   */
  isLoginAvailable(login: string): Observable<boolean> {
    return this.http
      .get<{ success?: string }>(`${this.resourceUrl}/login/available`, { params: new HttpParams().set('login', login) })
      .pipe(map(body => body.success !== 'Exists'));
  }

  isEmailAvailable(email: string): Observable<boolean> {
    return this.http
      .get<{ success?: string }>(`${this.resourceUrl}/email/available`, { params: new HttpParams().set('email', email) })
      .pipe(map(body => body.success !== 'Exists'));
  }
}
