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

describe('InvoiceRole e2e test', () => {
  const invoiceRolePageUrl = '/invoice-role';
  let username: string;
  let password: string;
  const invoiceRoleSample = {};

  let invoiceRole;
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
        organisationName: 'instead as meh',
        organisationShortName: 'only',
        salutation: 'since',
        firstName: 'Jonatan',
        middleName: 'aw unhealthy',
        lastName: 'Bosco',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'although round',
        primaryEmail: 'colorfully',
        isTemporaryPassword: true,
        logoImageUrl: 'across impeccable attest',
        profileImageUrl: 'operating honestly',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoice-items', {
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
    if (invoiceRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-roles/${invoiceRole.id}`,
      }).then(() => {
        invoiceRole = undefined;
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

  it('InvoiceRoles menu should load InvoiceRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceRole').should('exist');
    cy.location('pathname').should('eq', invoiceRolePageUrl);
  });

  describe('InvoiceRole page', () => {
    it('should have translated page title', () => {
      cy.visit(invoiceRolePageUrl);
      cy.getEntityHeading('InvoiceRole').should('not.contain', 'snpApp.invoiceRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoiceRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InvoiceRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-roles',
          body: {
            ...invoiceRoleSample,
            party,
          },
        }).then(({ body }) => {
          invoiceRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/invoice-roles?page=0&size=20>; rel="last",<http://localhost/api/invoice-roles?page=0&size=20>; rel="first"',
              },
              body: [invoiceRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InvoiceRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceRolePageUrl);
      });

      it('edit button click should load edit InvoiceRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceRolePageUrl);
      });

      it('edit button click should load edit InvoiceRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceRolePageUrl);
      });

      it('last delete button click should delete instance of InvoiceRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoiceRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceRolePageUrl);

        invoiceRole = undefined;
      });
    });
  });

  describe('new InvoiceRole page', () => {
    beforeEach(() => {
      cy.visit(invoiceRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceRole');
    });

    it('should create an instance of InvoiceRole', () => {
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoiceRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoiceRolePageUrl);
    });
  });
});
