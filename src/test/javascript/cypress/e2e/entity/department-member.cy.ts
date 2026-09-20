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

describe('DepartmentMember e2e test', () => {
  const departmentMemberPageUrl = '/department-member';
  let username: string;
  let password: string;
  const departmentMemberSample = { fromDate: '2023-12-14T16:13:14.750Z' };

  let departmentMember;
  let department;
  let party;

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
      url: '/api/departments',
      body: { name: 'generously except uncomfortable', code: 'jealously yogurt pai', description: 'quicker', isActive: true },
    }).then(({ body }) => {
      department = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'grim',
        organisationShortName: 'though into',
        salutation: 'nightlife common yuc',
        firstName: 'Thad',
        middleName: 'thorny',
        lastName: 'Vandervort-Lang',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'within sup tough',
        primaryEmail: 'jealous sympathetically',
        isTemporaryPassword: false,
        logoImageUrl: 'considering',
        profileImageUrl: 'off arrogantly',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/department-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/department-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/department-members/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/departments', {
      statusCode: 200,
      body: [department],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (departmentMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/department-members/${departmentMember.id}`,
      }).then(() => {
        departmentMember = undefined;
      });
    }
  });

  afterEach(() => {
    if (department) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/departments/${department.id}`,
      }).then(() => {
        department = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('DepartmentMembers menu should load DepartmentMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('department-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepartmentMember').should('exist');
    cy.location('pathname').should('eq', departmentMemberPageUrl);
  });

  describe('DepartmentMember page', () => {
    it('should have translated page title', () => {
      cy.visit(departmentMemberPageUrl);
      cy.getEntityHeading('DepartmentMember').should('not.contain', 'snpApp.departmentMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(departmentMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepartmentMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${departmentMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('DepartmentMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', departmentMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/department-members',
          body: {
            ...departmentMemberSample,
            department,
            party,
          },
        }).then(({ body }) => {
          departmentMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/department-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/department-members?page=0&size=20>; rel="last",<http://localhost/api/department-members?page=0&size=20>; rel="first"',
              },
              body: [departmentMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(departmentMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepartmentMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('departmentMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', departmentMemberPageUrl);
      });

      it('edit button click should load edit DepartmentMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepartmentMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', departmentMemberPageUrl);
      });

      it('edit button click should load edit DepartmentMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepartmentMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', departmentMemberPageUrl);
      });

      it('last delete button click should delete instance of DepartmentMember', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('departmentMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', departmentMemberPageUrl);

        departmentMember = undefined;
      });
    });
  });

  describe('new DepartmentMember page', () => {
    beforeEach(() => {
      cy.visit(departmentMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DepartmentMember');
    });

    it('should create an instance of DepartmentMember', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T01:45');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T01:45');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T11:08');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T11:08');

      cy.get(`[data-cy="isPrimary"]`).should('not.be.checked');
      cy.get(`[data-cy="isPrimary"]`).click();
      cy.get(`[data-cy="isPrimary"]`).should('be.checked');

      cy.get(`[data-cy="department"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        departmentMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', departmentMemberPageUrl);
    });
  });
});
