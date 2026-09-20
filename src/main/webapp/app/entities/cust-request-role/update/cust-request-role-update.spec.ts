import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { ICustRequestRole } from '../cust-request-role.model';
import { CustRequestRoleService } from '../service/cust-request-role.service';

import { CustRequestRoleFormService } from './cust-request-role-form.service';
import { CustRequestRoleUpdate } from './cust-request-role-update';

describe('CustRequestRole Management Update Component', () => {
  let comp: CustRequestRoleUpdate;
  let fixture: ComponentFixture<CustRequestRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestRoleFormService: CustRequestRoleFormService;
  let custRequestRoleService: CustRequestRoleService;
  let custRequestService: CustRequestService;
  let custRequestItemService: CustRequestItemService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CustRequestRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestRoleFormService = TestBed.inject(CustRequestRoleFormService);
    custRequestRoleService = TestBed.inject(CustRequestRoleService);
    custRequestService = TestBed.inject(CustRequestService);
    custRequestItemService = TestBed.inject(CustRequestItemService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequest query and add missing value', () => {
      const custRequestRole: ICustRequestRole = { id: 16150 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestRole.custRequest = custRequest;

      const custRequestCollection: ICustRequest[] = [{ id: 17760 }];
      vi.spyOn(custRequestService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCollection })));
      const additionalCustRequests = [custRequest];
      const expectedCollection: ICustRequest[] = [...additionalCustRequests, ...custRequestCollection];
      vi.spyOn(custRequestService, 'addCustRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      expect(custRequestService.query).toHaveBeenCalled();
      expect(custRequestService.addCustRequestToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCollection,
        ...additionalCustRequests.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustRequestItem query and add missing value', () => {
      const custRequestRole: ICustRequestRole = { id: 16150 };
      const custRequestItem: ICustRequestItem = { id: 17328 };
      custRequestRole.custRequestItem = custRequestItem;

      const custRequestItemCollection: ICustRequestItem[] = [{ id: 17328 }];
      vi.spyOn(custRequestItemService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestItemCollection })));
      const additionalCustRequestItems = [custRequestItem];
      const expectedCollection: ICustRequestItem[] = [...additionalCustRequestItems, ...custRequestItemCollection];
      vi.spyOn(custRequestItemService, 'addCustRequestItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      expect(custRequestItemService.query).toHaveBeenCalled();
      expect(custRequestItemService.addCustRequestItemToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestItemCollection,
        ...additionalCustRequestItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const custRequestRole: ICustRequestRole = { id: 16150 };
      const party: IParty = { id: 24527 };
      custRequestRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const custRequestRole: ICustRequestRole = { id: 16150 };
      const roleType: IRoleType = { id: 17576 };
      custRequestRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestRole: ICustRequestRole = { id: 16150 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestRole.custRequest = custRequest;
      const custRequestItem: ICustRequestItem = { id: 17328 };
      custRequestRole.custRequestItem = custRequestItem;
      const party: IParty = { id: 24527 };
      custRequestRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      custRequestRole.roleType = roleType;

      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      expect(comp.custRequestsSharedCollection()).toContainEqual(custRequest);
      expect(comp.custRequestItemsSharedCollection()).toContainEqual(custRequestItem);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.custRequestRole).toEqual(custRequestRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestRole>();
      const custRequestRole = { id: 26912 };
      vi.spyOn(custRequestRoleFormService, 'getCustRequestRole').mockReturnValue(custRequestRole);
      vi.spyOn(custRequestRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestRole);
      saveSubject.complete();

      // THEN
      expect(custRequestRoleFormService.getCustRequestRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestRoleService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestRole>();
      const custRequestRole = { id: 26912 };
      vi.spyOn(custRequestRoleFormService, 'getCustRequestRole').mockReturnValue({ id: null });
      vi.spyOn(custRequestRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestRole);
      saveSubject.complete();

      // THEN
      expect(custRequestRoleFormService.getCustRequestRole).toHaveBeenCalled();
      expect(custRequestRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestRole>();
      const custRequestRole = { id: 26912 };
      vi.spyOn(custRequestRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequest', () => {
      it('should forward to custRequestService', () => {
        const entity = { id: 17760 };
        const entity2 = { id: 10199 };
        vi.spyOn(custRequestService, 'compareCustRequest');
        comp.compareCustRequest(entity, entity2);
        expect(custRequestService.compareCustRequest).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustRequestItem', () => {
      it('should forward to custRequestItemService', () => {
        const entity = { id: 17328 };
        const entity2 = { id: 23718 };
        vi.spyOn(custRequestItemService, 'compareCustRequestItem');
        comp.compareCustRequestItem(entity, entity2);
        expect(custRequestItemService.compareCustRequestItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
