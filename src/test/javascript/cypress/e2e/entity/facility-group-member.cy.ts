import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('FacilityGroupMember e2e test', () => {
  const facilityGroupMemberPageUrl = '/facility-group-member';
  let username: string;
  let password: string;
  const facilityGroupMemberSample = { fromDate: '2023-12-14T08:36:59.614Z' };

  let facilityGroupMember;
  let facility;
  let facilityGroup;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/facilities',
      body: { name: 'violently', description: 'off same gosh', facilitySize: 7790.47 },
    }).then(({ body }) => {
      facility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/facility-groups',
      body: { name: 'drat heroine for', description: 'yum punctual small' },
    }).then(({ body }) => {
      facilityGroup = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-group-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-group-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-group-members/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [facility],
    });

    cy.intercept('GET', '/api/facility-groups', {
      statusCode: 200,
      body: [facilityGroup],
    });
  });

  afterEach(() => {
    if (facilityGroupMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-group-members/${facilityGroupMember.id}`,
      }).then(() => {
        facilityGroupMember = undefined;
      });
    }
  });

  afterEach(() => {
    if (facility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facilities/${facility.id}`,
      }).then(() => {
        facility = undefined;
      });
    }
    if (facilityGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-groups/${facilityGroup.id}`,
      }).then(() => {
        facilityGroup = undefined;
      });
    }
  });

  it('FacilityGroupMembers menu should load FacilityGroupMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-group-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityGroupMember').should('exist');
    cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
  });

  describe('FacilityGroupMember page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityGroupMemberPageUrl);
      cy.getEntityHeading('FacilityGroupMember').should('not.contain', 'snpApp.facilityGroupMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityGroupMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityGroupMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityGroupMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityGroupMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-group-members',
          body: {
            ...facilityGroupMemberSample,
            facility,
            facilityGroup,
          },
        }).then(({ body }) => {
          facilityGroupMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-group-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/facility-group-members?page=0&size=20>; rel="last",<http://localhost/api/facility-group-members?page=0&size=20>; rel="first"',
              },
              body: [facilityGroupMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityGroupMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityGroupMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityGroupMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
      });

      it('edit button click should load edit FacilityGroupMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroupMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
      });

      it('edit button click should load edit FacilityGroupMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroupMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
      });

      it('last delete button click should delete instance of FacilityGroupMember', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityGroupMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupMemberPageUrl);

        facilityGroupMember = undefined;
      });
    });
  });

  describe('new FacilityGroupMember page', () => {
    beforeEach(() => {
      cy.visit(facilityGroupMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityGroupMember');
    });

    it('should create an instance of FacilityGroupMember', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T15:13');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T15:13');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T06:56');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T06:56');

      cy.get(`[data-cy="sequenceNo"]`).type('22531');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '22531');

      cy.get(`[data-cy="facility"]`).select(1);
      cy.get(`[data-cy="facilityGroup"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityGroupMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityGroupMemberPageUrl);
    });
  });
});
