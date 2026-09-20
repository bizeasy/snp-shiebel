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

describe('CustRequestRole e2e test', () => {
  const custRequestRolePageUrl = '/cust-request-role';
  let username: string;
  let password: string;
  const custRequestRoleSample = {};

  let custRequestRole;
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
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'excluding acidly ape',
        organisationShortName: 'after shoulder swift',
        salutation: 'deform',
        firstName: 'Pearline',
        middleName: 'tough oh nor',
        lastName: 'Fahey',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'midst weep',
        primaryEmail: 'soft cow lest',
        isTemporaryPassword: true,
        logoImageUrl: 'pneumonia',
        profileImageUrl: 'sway although icebreaker',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cust-requests', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cust-request-items', {
      statusCode: 200,
      body: [],
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
    if (custRequestRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-roles/${custRequestRole.id}`,
      }).then(() => {
        custRequestRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('CustRequestRoles menu should load CustRequestRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestRole').should('exist');
    cy.location('pathname').should('eq', custRequestRolePageUrl);
  });

  describe('CustRequestRole page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestRolePageUrl);
      cy.getEntityHeading('CustRequestRole').should('not.contain', 'snpApp.custRequestRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-roles',
          body: {
            ...custRequestRoleSample,
            party,
          },
        }).then(({ body }) => {
          custRequestRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cust-request-roles?page=0&size=20>; rel="last",<http://localhost/api/cust-request-roles?page=0&size=20>; rel="first"',
              },
              body: [custRequestRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustRequestRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestRolePageUrl);
      });

      it('edit button click should load edit CustRequestRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestRolePageUrl);
      });

      it('edit button click should load edit CustRequestRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestRolePageUrl);
      });

      it('last delete button click should delete instance of CustRequestRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestRolePageUrl);

        custRequestRole = undefined;
      });
    });
  });

  describe('new CustRequestRole page', () => {
    beforeEach(() => {
      cy.visit(custRequestRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestRole');
    });

    it('should create an instance of CustRequestRole', () => {
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestRolePageUrl);
    });
  });
});
